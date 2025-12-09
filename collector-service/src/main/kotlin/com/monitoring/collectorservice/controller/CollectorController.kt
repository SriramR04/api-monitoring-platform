package com.monitoring.collectorservice.controller

import com.monitoring.collectorservice.dto.ApiLogDto
import com.monitoring.collectorservice.dto.RateLimitEventDto
import com.monitoring.collectorservice.model.logs.ApiLog
import com.monitoring.collectorservice.model.logs.RateLimitEvent
import com.monitoring.collectorservice.service.LogCollectorService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/collector")
class CollectorController(
    private val logCollectorService: LogCollectorService
) {

    @PostMapping("/logs")
    fun receiveLog(@Valid @RequestBody logDto: ApiLogDto): ResponseEntity<ApiLog> {
        val savedLog = logCollectorService.collectApiLog(logDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLog)
    }

    @PostMapping("/rate-limit-events")
    fun receiveRateLimitEvent(@Valid @RequestBody eventDto: RateLimitEventDto): ResponseEntity<RateLimitEvent> {
        val savedEvent = logCollectorService.collectRateLimitEvent(eventDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent)
    }

    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf(
            "status" to "UP",
            "endpoint" to "collector"
        ))
    }
}