package com.monitoring.demo.dto

import java.time.Instant

data class ApiLogDto(
    val serviceName: String,
    val endpoint: String,
    val method: String,
    val statusCode: Int,
    val requestSize: Long = 0,
    val responseSize: Long = 0,
    val latency: Long,
    val timestamp: Instant = Instant.now()
)

data class RateLimitEventDto(
    val serviceName: String,
    val endpoint: String,
    val configuredLimit: Int,
    val actualRequestCount: Int,
    val timestamp: Instant = Instant.now()
)