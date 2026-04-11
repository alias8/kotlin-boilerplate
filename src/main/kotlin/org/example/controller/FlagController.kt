package org.example.controller

import jakarta.servlet.http.HttpServletRequest
import org.example.dto.FlagAdjustEventData
import org.example.model.Flag
import org.example.model.FlagOverride
import org.example.repository.ApiKeyRepository
import org.example.repository.FlagOverrideRepository
import org.example.repository.FlagRepository
import org.example.service.SseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class FlagController(
    private val apiKeyRepository: ApiKeyRepository,
    private val flagRepository: FlagRepository,
    private val flagOverrideRepository: FlagOverrideRepository,
    private val sseService: SseService
) {

    @PatchMapping("/flags")
    @Transactional
    fun updateFlag(request: HttpServletRequest, @RequestBody patchData: FlagAdjustEventData): ResponseEntity<Void> {
        val orgId = checkAuth(request) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        // Find or create flag
        var flag = flagRepository.findByOrgIdAndName(orgId, patchData.name)
        if (flag == null) {
            flag = Flag(
                name = patchData.name,
                enabled = patchData.enabled,
                orgId = orgId,
                rolloutPercentage = patchData.rolloutPercentage
            )
        } else {
            flag.enabled = patchData.enabled
            flag.rolloutPercentage = patchData.rolloutPercentage
        }
        flag = flagRepository.save(flag)

        // Replace all overrides: delete existing, insert new
        flagOverrideRepository.deleteByFlagId(flag.id)
        for (o in patchData.flagOverrides) {
            flagOverrideRepository.save(
                FlagOverride(
                    userId = o.userId,
                    override = o.override,
                    flagId = flag.id
                )
            )
        }

        // Send the update to all connected /stream clients
        sseService.broadcast(orgId, patchData)

        return ResponseEntity.ok().build()
    }

    private fun checkAuth(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization") ?: return null
        if (!authHeader.startsWith("Bearer ")) return null
        val token = authHeader.removePrefix("Bearer ")
        val apiKey = apiKeyRepository.findByToken(token) ?: return null
        return apiKey.orgId
    }
}
