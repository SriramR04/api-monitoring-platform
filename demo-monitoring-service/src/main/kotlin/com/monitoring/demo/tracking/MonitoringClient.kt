package com.monitoring.demo.tracking

import com.monitoring.demo.dto.ApiLogDto
import com.monitoring.demo.dto.RateLimitEventDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class MonitoringClient {

    @Value("\${monitoring.collector.url}")
    private lateinit var collectorUrl: String

    private val restTemplate = RestTemplate()

    fun sendLog(logDto: ApiLogDto) {
        try {
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON

            val request = HttpEntity(logDto, headers)
            restTemplate.postForEntity("$collectorUrl/logs", request, String::class.java)
            
        } catch (e: Exception) {
            println("⚠️ Failed to send log to collector: ${e.message}")
        }
    }

    fun sendRateLimitEvent(eventDto: RateLimitEventDto) {
        try {
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON

            val request = HttpEntity(eventDto, headers)
            restTemplate.postForEntity("$collectorUrl/rate-limit-events", request, String::class.java)
            
        } catch (e: Exception) {
            println("⚠️ Failed to send rate limit event: ${e.message}")
        }
    }
}