package com.monitoring.collectorservice.controller

import com.monitoring.collectorservice.model.logs.ApiLog
import com.monitoring.collectorservice.service.LogCollectorService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/logs")
class LogController(
    private val logCollectorService: LogCollectorService
) {

    @GetMapping
    fun getAllLogs(): ResponseEntity<List<ApiLog>> {
        val logs = logCollectorService.getAllLogs()
        return ResponseEntity.ok(logs)
    }

    @GetMapping("/service/{serviceName}")
    fun getLogsByService(@PathVariable serviceName: String): ResponseEntity<List<ApiLog>> {
        val logs = logCollectorService.getLogsByService(serviceName)
        return ResponseEntity.ok(logs)
    }

    @GetMapping("/slow")
    fun getSlowApis(): ResponseEntity<List<ApiLog>> {
        val logs = logCollectorService.getSlowApis()
        return ResponseEntity.ok(logs)
    }

    @GetMapping("/broken")
    fun getBrokenApis(): ResponseEntity<List<ApiLog>> {
        val logs = logCollectorService.getBrokenApis()
        return ResponseEntity.ok(logs)
    }

    @GetMapping("/date-range")
    fun getLogsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: Instant,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) end: Instant
    ): ResponseEntity<List<ApiLog>> {
        val logs = logCollectorService.getLogsByDateRange(start, end)
        return ResponseEntity.ok(logs)
    }
}