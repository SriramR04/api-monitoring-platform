package com.monitoring.collectorservice.dto

import com.monitoring.collectorservice.model.metadata.AlertStatus
import com.monitoring.collectorservice.model.metadata.AlertType
import java.time.Instant

data class AlertDto(
    val id: String,
    val serviceName: String,
    val endpoint: String,
    val alertType: AlertType,
    val status: AlertStatus,
    val timestamp: Instant,
    val details: String,
    val resolvedAt: Instant? = null,
    val resolvedBy: String? = null,
    val resolutionCount: Int = 0
)

data class ResolveAlertRequest(
    val resolvedBy: String  // username of developer
)

data class DashboardStats(
    val totalLogs: Long,
    val slowApiCount: Long,
    val brokenApiCount: Long,
    val rateLimitViolations: Long,
    val activeAlerts: Long,
    val resolvedAlerts: Long
)