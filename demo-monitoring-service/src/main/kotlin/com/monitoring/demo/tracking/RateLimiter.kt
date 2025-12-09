package com.monitoring.demo.tracking

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Component
class RateLimiter {

    @Value("\${monitoring.rateLimit.service}")
    private lateinit var serviceName: String

    @Value("\${monitoring.rateLimit.limit}")
    private var limit: Int = 100

    @Value("\${monitoring.rateLimit.enabled}")
    private var enabled: Boolean = true

    // Track requests per second
    private val requestCounts = ConcurrentHashMap<Long, AtomicInteger>()
    private var lastCleanupTime = System.currentTimeMillis()

    fun checkRateLimit(endpoint: String): RateLimitResult {
        if (!enabled) {
            return RateLimitResult(false, 0)
        }

        val currentSecond = System.currentTimeMillis() / 1000
        val count = requestCounts.computeIfAbsent(currentSecond) { AtomicInteger(0) }
        val currentCount = count.incrementAndGet()

        // Cleanup old entries every 5 seconds
        if (System.currentTimeMillis() - lastCleanupTime > 5000) {
            cleanup(currentSecond)
        }

        val limitExceeded = currentCount > limit

        return RateLimitResult(
            limitExceeded = limitExceeded,
            currentCount = currentCount,
            serviceName = serviceName,
            endpoint = endpoint,
            configuredLimit = limit
        )
    }

    private fun cleanup(currentSecond: Long) {
        requestCounts.keys.removeIf { it < currentSecond - 2 }
        lastCleanupTime = System.currentTimeMillis()
    }
}

data class RateLimitResult(
    val limitExceeded: Boolean,
    val currentCount: Int,
    val serviceName: String = "",
    val endpoint: String = "",
    val configuredLimit: Int = 0
)