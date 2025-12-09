package com.monitoring.collectorservice.service

import com.monitoring.collectorservice.dto.AlertDto
import com.monitoring.collectorservice.dto.DashboardStats
import com.monitoring.collectorservice.dto.ResolveAlertRequest
import com.monitoring.collectorservice.model.logs.ApiLog
import com.monitoring.collectorservice.model.logs.RateLimitEvent
import com.monitoring.collectorservice.model.metadata.Alert
import com.monitoring.collectorservice.model.metadata.AlertStatus
import com.monitoring.collectorservice.model.metadata.AlertType
import com.monitoring.collectorservice.repository.logs.ApiLogRepository
import com.monitoring.collectorservice.repository.logs.RateLimitEventRepository
import com.monitoring.collectorservice.repository.metadata.AlertRepository
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class AlertService(
    private val alertRepository: AlertRepository,
    private val apiLogRepository: ApiLogRepository,
    private val rateLimitEventRepository: RateLimitEventRepository
) {

    fun createSlowApiAlert(apiLog: ApiLog) {
        val alert = Alert(
            serviceName = apiLog.serviceName,
            endpoint = apiLog.endpoint,
            alertType = AlertType.SLOW_API,
            status = AlertStatus.ACTIVE,
            timestamp = apiLog.timestamp,
            details = "Latency: ${apiLog.latency}ms (threshold: 500ms)"
        )
        alertRepository.save(alert)
    }

    fun createBrokenApiAlert(apiLog: ApiLog) {
        val alert = Alert(
            serviceName = apiLog.serviceName,
            endpoint = apiLog.endpoint,
            alertType = AlertType.BROKEN_API,
            status = AlertStatus.ACTIVE,
            timestamp = apiLog.timestamp,
            details = "Status Code: ${apiLog.statusCode}"
        )
        alertRepository.save(alert)
    }

    fun createRateLimitAlert(event: RateLimitEvent) {
        val alert = Alert(
            serviceName = event.serviceName,
            endpoint = event.endpoint,
            alertType = AlertType.RATE_LIMIT_VIOLATION,
            status = AlertStatus.ACTIVE,
            timestamp = event.timestamp,
            details = "Limit: ${event.configuredLimit}/s, Actual: ${event.actualRequestCount}/s"
        )
        alertRepository.save(alert)
    }

    fun getAllAlerts(): List<AlertDto> {
        return alertRepository.findAll().map { it.toDto() }
    }

    fun getActiveAlerts(): List<AlertDto> {
        return alertRepository.findByStatus(AlertStatus.ACTIVE).map { it.toDto() }
    }

    fun getAlertsByType(type: AlertType): List<AlertDto> {
        return alertRepository.findByAlertType(type).map { it.toDto() }
    }

    fun resolveAlert(alertId: String, request: ResolveAlertRequest): AlertDto {
        var retries = 3
        
        while (retries > 0) {
            try {
                val alert = alertRepository.findById(alertId)
                    .orElseThrow { RuntimeException("Alert not found: $alertId") }

                val updatedAlert = alert.copy(
                    status = AlertStatus.RESOLVED,
                    resolvedAt = Instant.now(),
                    resolvedBy = request.resolvedBy,
                    resolutionCount = alert.resolutionCount + 1
                )

                return alertRepository.save(updatedAlert).toDto()
            } catch (e: OptimisticLockingFailureException) {
                retries--
                if (retries == 0) throw e
                Thread.sleep(50)
            }
        }
        
        throw RuntimeException("Failed to resolve alert after retries")
    }

    fun getDashboardStats(): DashboardStats {
        return DashboardStats(
            totalLogs = apiLogRepository.count(),
            slowApiCount = apiLogRepository.countByIsSlowApiTrue(),
            brokenApiCount = apiLogRepository.countByIsBrokenApiTrue(),
            rateLimitViolations = rateLimitEventRepository.count(),
            activeAlerts = alertRepository.countByStatus(AlertStatus.ACTIVE),
            resolvedAlerts = alertRepository.countByStatus(AlertStatus.RESOLVED)
        )
    }

    private fun Alert.toDto() = AlertDto(
        id = this.id!!,
        serviceName = this.serviceName,
        endpoint = this.endpoint,
        alertType = this.alertType,
        status = this.status,
        timestamp = this.timestamp,
        details = this.details,
        resolvedAt = this.resolvedAt,
        resolvedBy = this.resolvedBy,
        resolutionCount = this.resolutionCount
    )
}