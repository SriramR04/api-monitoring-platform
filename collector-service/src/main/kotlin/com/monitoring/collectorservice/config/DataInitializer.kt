package com.monitoring.collectorservice.config

import com.monitoring.collectorservice.service.AuthService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataInitializer {

    @Bean
    fun initializeData(authService: AuthService): CommandLineRunner {
        return CommandLineRunner {
            println("=".repeat(50))
            println("🚀 Initializing Collector Service...")
            authService.initializeDefaultUser()
            println("=".repeat(50))
        }
    }
}