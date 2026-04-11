package org.example.repository

import org.example.model.ApiKey
import org.springframework.data.jpa.repository.JpaRepository

interface ApiKeyRepository : JpaRepository<ApiKey, String> {
    fun findByToken(token: String): ApiKey?
}
