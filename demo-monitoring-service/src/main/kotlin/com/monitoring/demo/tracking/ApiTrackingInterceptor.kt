package com.monitoring.demo.tracking

import com.monitoring.demo.dto.ApiLogDto
import com.monitoring.demo.dto.RateLimitEventDto
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Instant

@Component
class ApiTrackingInterceptor(
    private val monitoringClient: MonitoringClient,
    private val rateLimiter: RateLimiter
) : HandlerInterceptor {

    @Value("\${spring.application.name}")
    private lateinit var serviceName: String

    companion object {
        private const val START_TIME_ATTR = "startTime"
    }

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        // Record start time
        request.setAttribute(START_TIME_ATTR, System.currentTimeMillis())

        // Check rate limit
        val rateLimitResult = rateLimiter.checkRateLimit(request.requestURI)
        
        if (rateLimitResult.limitExceeded) {
            // Send rate limit event
            val rateLimitEvent = RateLimitEventDto(
                serviceName = rateLimitResult.serviceName,
                endpoint = request.requestURI,
                configuredLimit = rateLimitResult.configuredLimit,
                actualRequestCount = rateLimitResult.currentCount,
                timestamp = Instant.now()
            )
            monitoringClient.sendRateLimitEvent(rateLimitEvent)
        }

        // Continue processing the request
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val startTime = request.getAttribute(START_TIME_ATTR) as? Long ?: return
        val endTime = System.currentTimeMillis()
        val latency = endTime - startTime

        // Calculate request/response sizes (approximate)
        val requestSize = calculateRequestSize(request)
        val responseSize = calculateResponseSize(response)

        // Create API log
        val apiLog = ApiLogDto(
            serviceName = serviceName,
            endpoint = request.requestURI,
            method = request.method,
            statusCode = response.status,
            requestSize = requestSize,
            responseSize = responseSize,
            latency = latency,
            timestamp = Instant.now()
        )

        // Send log to collector asynchronously
        Thread {
            monitoringClient.sendLog(apiLog)
        }.start()

        // Log to console
        println("📊 [${apiLog.method}] ${apiLog.endpoint} - ${apiLog.statusCode} - ${apiLog.latency}ms")
    }

    private fun calculateRequestSize(request: HttpServletRequest): Long {
        var size = 0L
        
        // Headers
        request.headerNames.asIterator().forEach { headerName ->
            size += headerName.length
            request.getHeaders(headerName).asIterator().forEach { headerValue ->
                size += headerValue.length
            }
        }
        
        // Query string
        request.queryString?.let { size += it.length }
        
        // Content length
        if (request.contentLength > 0) {
            size += request.contentLength
        }
        
        return size
    }

    private fun calculateResponseSize(response: HttpServletResponse): Long {
        var size = 0L
        
        // Headers
        response.headerNames.forEach { headerName ->
            size += headerName.length
            response.getHeaders(headerName).forEach { headerValue ->
                size += headerValue.length
            }
        }
        
        // Content length if available
        val contentLength = response.getHeader("Content-Length")
        contentLength?.toLongOrNull()?.let { size += it }
        
        return size
    }
}