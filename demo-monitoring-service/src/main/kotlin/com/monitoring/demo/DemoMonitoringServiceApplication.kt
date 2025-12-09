package com.monitoring.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoMonitoringServiceApplication

fun main(args: Array<String>) {
    runApplication<DemoMonitoringServiceApplication>(*args)
    println("=".repeat(60))
    println("🚀 Demo Monitoring Service Started on port 8081")
    println("=".repeat(60))
    println("📌 Available Endpoints:")
    println("   GET http://localhost:8081/api/demo/success  (Fast API)")
    println("   GET http://localhost:8081/api/demo/slow     (Slow API - Triggers Alert)")
    println("   GET http://localhost:8081/api/demo/error    (Error API - Triggers Alert)")
    println("   GET http://localhost:8081/api/demo/health   (Health Check)")
    println("=".repeat(60))
}