package org.example.repository

import org.example.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
}
