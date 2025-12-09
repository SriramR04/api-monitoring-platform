package com.monitoring.collectorservice.controller

import com.monitoring.collectorservice.dto.LoginRequest
import com.monitoring.collectorservice.dto.LoginResponse
import com.monitoring.collectorservice.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return try {
            val response = authService.login(loginRequest)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            ResponseEntity.status(401).build()
        }
    }

    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf(
            "status" to "UP",
            "service" to "collector-service"
        ))
    }
}