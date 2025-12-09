package com.monitoring.collectorservice.service

import com.monitoring.collectorservice.dto.ApiLogDto
import com.monitoring.collectorservice.dto.RateLimitEventDto
import com.monitoring.collectorservice.model.logs.ApiLog
import com.monitoring.collectorservice.model.logs.RateLimitEvent
import com.monitoring.collectorservice.repository.logs.ApiLogRepository
import com.monitoring.collectorservice.repository.logs.RateLimitEventRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class LogCollectorService(
    private val apiLogRepository: ApiLogRepository,
    private val rateLimitEventRepository: RateLimitEventRepository,
    private val alertService: AlertService
) {

    fun collectApiLog(logDto: ApiLogDto): ApiLog {
        val isSlowApi = logDto.latency > 500
        val isBrokenApi = logDto.statusCode >= 500

        val apiLog = ApiLog(
            serviceName = logDto.serviceName,
            endpoint = logDto.endpoint,
            method = logDto.method,
            statusCode = logDto.statusCode,
            requestSize = logDto.requestSize,
            responseSize = logDto.responseSize,
            latency = logDto.latency,
            timestamp = logDto.timestamp,
            isSlowApi = isSlowApi,
            isBrokenApi = isBrokenApi
        )

        val savedLog = apiLogRepository.save(apiLog)

        if (isSlowApi) {
            alertService.createSlowApiAlert(savedLog)
        }
        if (isBrokenApi) {
            alertService.createBrokenApiAlert(savedLog)
        }

        return savedLog
    }

    fun collectRateLimitEvent(eventDto: RateLimitEventDto): RateLimitEvent {
        val event = RateLimitEvent(
            serviceName = eventDto.serviceName,
            endpoint = eventDto.endpoint,
            timestamp = eventDto.timestamp,
            configuredLimit = eventDto.configuredLimit,
            actualRequestCount = eventDto.actualRequestCount
        )

        val savedEvent = rateLimitEventRepository.save(event)
        alertService.createRateLimitAlert(savedEvent)

        return savedEvent
    }

    fun getAllLogs(): List<ApiLog> = apiLogRepository.findAll()

    fun getLogsByService(serviceName: String): List<ApiLog> =
        apiLogRepository.findByServiceName(serviceName)

    fun getSlowApis(): List<ApiLog> = apiLogRepository.findByIsSlowApiTrue()

    fun getBrokenApis(): List<ApiLog> = apiLogRepository.findByIsBrokenApiTrue()

    fun getLogsByDateRange(start: Instant, end: Instant): List<ApiLog> =
        apiLogRepository.findByTimestampBetween(start, end)
}