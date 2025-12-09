package com.monitoring.demo.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/demo")
class DemoApiController {

    /**
     * Fast API - Normal response (< 500ms)
     * This will NOT trigger a slow API alert
     */
    @GetMapping("/success")
    fun successEndpoint(): ResponseEntity<Map<String, Any>> {
        val response = mapOf(
            "status" to "success",
            "message" to "This is a fast API endpoint",
            "timestamp" to Instant.now().toString(),
            "latency" to "< 100ms"
        )
        return ResponseEntity.ok(response)
    }

    /**
     * Slow API - Intentional delay (> 500ms)
     * This WILL trigger a slow API alert in the collector
     */
    @GetMapping("/slow")
    fun slowEndpoint(): ResponseEntity<Map<String, Any>> {
        // Simulate slow processing
        Thread.sleep(850) // 850ms delay

        val response = mapOf(
            "status" to "success",
            "message" to "This API is intentionally slow",
            "timestamp" to Instant.now().toString(),
            "latency" to "~850ms",
            "alert" to "This should trigger a SLOW API alert"
        )
        return ResponseEntity.ok(response)
    }

    /**
     * Error API - Returns 500 Internal Server Error
     * This WILL trigger a broken API alert in the collector
     */
    @GetMapping("/error")
    fun errorEndpoint(): ResponseEntity<Map<String, Any>> {
        val response = mapOf(
            "status" to "error",
            "message" to "Simulated internal server error",
            "timestamp" to Instant.now().toString(),
            "alert" to "This should trigger a BROKEN API alert"
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf(
            "status" to "UP",
            "service" to "demo-monitoring-service"
        ))
    }
}