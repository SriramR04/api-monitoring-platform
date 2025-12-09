package com.monitoring.collectorservice.service

import com.monitoring.collectorservice.dto.LoginRequest
import com.monitoring.collectorservice.dto.LoginResponse
import com.monitoring.collectorservice.model.metadata.User
import com.monitoring.collectorservice.repository.metadata.UserRepository
import com.monitoring.collectorservice.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
) {

    fun login(loginRequest: LoginRequest): LoginResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password
            )
        )

        val token = jwtTokenProvider.generateToken(loginRequest.username)
        val user = userRepository.findByUsername(loginRequest.username)
            ?: throw RuntimeException("User not found")

        return LoginResponse(
            token = token,
            username = user.username,
            email = user.email,
            expiresIn = jwtTokenProvider.getExpirationTime()
        )
    }

    fun initializeDefaultUser() {
        if (!userRepository.existsByUsername("admin")) {
            val defaultUser = User(
                username = "admin",
                password = passwordEncoder.encode("admin123"),
                email = "admin@monitoring.com",
                role = "DEVELOPER"
            )
            userRepository.save(defaultUser)
            println("✅ Default user created: username=admin, password=admin123")
        }
    }
}