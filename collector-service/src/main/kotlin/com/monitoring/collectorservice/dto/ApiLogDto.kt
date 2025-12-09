package com.monitoring.collectorservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.time.Instant

data class ApiLogDto(
    @field:NotBlank(message = "Service name is required")
    val serviceName: String,
    
    @field:NotBlank(message = "Endpoint is required")
    val endpoint: String,
    
    @field:NotBlank(message = "Method is required")
    val method: String,
    
    @field:Positive(message = "Status code must be positive")
    val statusCode: Int,
    
    val requestSize: Long = 0,
    val responseSize: Long = 0,
    
    @field:Positive(message = "Latency must be positive")
    val latency: Long,
    
    val timestamp: Instant = Instant.now()
)

data class RateLimitEventDto(
    @field:NotBlank(message = "Service name is required")
    val serviceName: String,
    
    @field:NotBlank(message = "Endpoint is required")
    val endpoint: String,
    
    val configuredLimit: Int,
    val actualRequestCount: Int,
    val timestamp: Instant = Instant.now()
)