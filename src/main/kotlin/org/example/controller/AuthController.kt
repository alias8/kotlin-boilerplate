package org.example.controller

import org.example.dto.AuthResponse
import org.example.dto.LoginRequest
import org.example.dto.RegisterRequest
import org.example.model.User
import org.example.repository.UserRepository
import org.example.security.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        if (userRepository.existsByUsername(request.username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
        val user = User(username = request.username, passwordHash = passwordEncoder.encode(request.password))
        userRepository.save(user)
        val token = jwtUtil.generate(user.username)
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse(token))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        val user = userRepository.findByUsername(request.username)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        val token = jwtUtil.generate(user.username)
        return ResponseEntity.ok(AuthResponse(token))
    }
}
