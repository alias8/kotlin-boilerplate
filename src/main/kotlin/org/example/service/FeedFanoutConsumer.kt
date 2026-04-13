package org.example.service

import jakarta.transaction.Transactional
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class FeedFanoutConsumer(
) {
    @Transactional
    @KafkaListener(topics = ["post-created"], groupId = "feed-fanout-group")
    fun consume(message: String) {
        // this is where async work will happen async write this
        listOf("").forEach { item ->
            // save something to db
        }
    }
}