package com.monitoring.collectorservice.model.metadata

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.index.Indexed
import java.time.Instant

enum class AlertType {
    SLOW_API,           // latency > 500ms
    BROKEN_API,         // 5xx status
    RATE_LIMIT_VIOLATION
}

enum class AlertStatus {
    ACTIVE,
    RESOLVED
}

@Document(collection = "alerts")
data class Alert(
    @Id
    val id: String? = null,
    
    val serviceName: String,
    val endpoint: String,
    val alertType: AlertType,
    
    @Indexed
    val status: AlertStatus = AlertStatus.ACTIVE,
    
    val timestamp: Instant = Instant.now(),
    val details: String,  // e.g., "Latency: 850ms" or "Status: 503"
    
    // For concurrency control (optimistic locking)
    @Version
    val version: Long? = null,
    
    // Resolution tracking
    val resolvedAt: Instant? = null,
    val resolvedBy: String? = null,
    val resolutionCount: Int = 0  // Track how many times marked resolved
)