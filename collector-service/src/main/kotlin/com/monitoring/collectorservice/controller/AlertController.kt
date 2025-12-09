package com.monitoring.collectorservice.controller

import com.monitoring.collectorservice.dto.AlertDto
import com.monitoring.collectorservice.dto.DashboardStats
import com.monitoring.collectorservice.dto.ResolveAlertRequest
import com.monitoring.collectorservice.model.metadata.AlertType
import com.monitoring.collectorservice.service.AlertService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/alerts")
class AlertController(
    private val alertService: AlertService
) {

    @GetMapping
    fun getAllAlerts(): ResponseEntity<List<AlertDto>> {
        val alerts = alertService.getAllAlerts()
        return ResponseEntity.ok(alerts)
    }

    @GetMapping("/active")
    fun getActiveAlerts(): ResponseEntity<List<AlertDto>> {
        val alerts = alertService.getActiveAlerts()
        return ResponseEntity.ok(alerts)
    }

    @GetMapping("/type/{type}")
    fun getAlertsByType(@PathVariable type: AlertType): ResponseEntity<List<AlertDto>> {
        val alerts = alertService.getAlertsByType(type)
        return ResponseEntity.ok(alerts)
    }

    @PutMapping("/{alertId}/resolve")
    fun resolveAlert(
        @PathVariable alertId: String,
        @RequestBody request: ResolveAlertRequest
    ): ResponseEntity<AlertDto> {
        val alert = alertService.resolveAlert(alertId, request)
        return ResponseEntity.ok(alert)
    }

    @GetMapping("/stats")
    fun getDashboardStats(): ResponseEntity<DashboardStats> {
        val stats = alertService.getDashboardStats()
        return ResponseEntity.ok(stats)
    }
}