package org.example.service

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class PostEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    fun publishPostCreated(postId: String) {
        kafkaTemplate.send("post-created", postId)
    }
}