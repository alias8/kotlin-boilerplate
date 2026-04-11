package org.example.service

import org.example.dto.FlagAdjustEventData
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

@Service
class SseService {

    // Fan-out pattern: one producer (/flags handler) broadcasts to many consumers (connected /stream clients)
    // Map of orgId -> list of SseEmitters for that org
    private val clients = ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>>()

    fun addClient(orgId: String, emitter: SseEmitter) {
        clients.computeIfAbsent(orgId) { CopyOnWriteArrayList() }.add(emitter)
    }

    fun removeClient(orgId: String, emitter: SseEmitter) {
        val orgClients = clients[orgId] ?: return
        orgClients.remove(emitter)
        if (orgClients.isEmpty()) {
            clients.remove(orgId)
        }
    }

    fun broadcast(orgId: String, patchData: FlagAdjustEventData) {
        val orgClients = clients[orgId] ?: return
        for (emitter in orgClients) {
            try {
                emitter.send(
                    SseEmitter.event()
                        .name("patch")
                        .data(patchData)
                )
            } catch (e: Exception) {
                // Client likely disconnected, will be cleaned up by onCompletion/onError
            }
        }
    }
}
