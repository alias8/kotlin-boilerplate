package org.example.controller

import jakarta.servlet.http.HttpServletRequest
import org.example.dto.FlagData
import org.example.dto.OverrideData
import org.example.dto.PutEventData
import org.example.repository.ApiKeyRepository
import org.example.repository.FlagRepository
import org.example.service.SseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@RestController
class StreamController(
    private val apiKeyRepository: ApiKeyRepository,
    private val flagRepository: FlagRepository,
    private val sseService: SseService
) {

    private val pingExecutor = Executors.newScheduledThreadPool(1)

    @GetMapping("/stream")
    fun stream(request: HttpServletRequest): Any {
        val orgId = checkAuth(request)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized")

        // No timeout — connection stays open indefinitely
        val emitter = SseEmitter(0L)

        // Send all flag data on connect ("put" event)
        val flags = flagRepository.findByOrgId(orgId)
        val flagDataList = flags.map { flag ->
            FlagData(
                name = flag.name,
                enabled = flag.enabled,
                rolloutPercentage = flag.rolloutPercentage,
                flagOverrides = flag.flagOverrides.map { OverrideData(it.userId, it.override) }
            )
        }
        val putData = PutEventData(orgId = orgId, flags = flagDataList)
        emitter.send(
            SseEmitter.event()
                .name("put")
                .data(putData)
        )

        // Register this client for future broadcast updates
        sseService.addClient(orgId, emitter)

        // Cleanup on disconnect
        val cleanup = Runnable {
            println("Client disconnected")
            sseService.removeClient(orgId, emitter)
        }
        emitter.onCompletion(cleanup)
        emitter.onError { cleanup.run() }
        emitter.onTimeout { cleanup.run() }

        // Send keepalive ping every 5 seconds
        val pingTask = pingExecutor.scheduleAtFixedRate({
            try {
                emitter.send(
                    SseEmitter.event()
                        .name("ping")
                        .data("{}")
                )
            } catch (e: Exception) {
                // Client disconnected, emitter callbacks will handle cleanup
            }
        }, 5, 5, TimeUnit.SECONDS)

        // Cancel ping task when emitter completes
        emitter.onCompletion { pingTask.cancel(false) }

        return emitter
    }

    private fun checkAuth(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization") ?: return null
        if (!authHeader.startsWith("Bearer ")) return null
        val token = authHeader.removePrefix("Bearer ")
        val apiKey = apiKeyRepository.findByToken(token) ?: return null
        return apiKey.orgId
    }
}
