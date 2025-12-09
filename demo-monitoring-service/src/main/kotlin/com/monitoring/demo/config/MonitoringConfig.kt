package com.monitoring.demo.config

import com.monitoring.demo.tracking.ApiTrackingInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class MonitoringConfig(
    private val apiTrackingInterceptor: ApiTrackingInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(apiTrackingInterceptor)
            .addPathPatterns("/api/**")
    }
}