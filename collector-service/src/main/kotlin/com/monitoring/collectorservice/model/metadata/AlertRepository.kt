package com.monitoring.collectorservice.repository.metadata

import com.monitoring.collectorservice.model.metadata.Alert
import com.monitoring.collectorservice.model.metadata.AlertStatus
import com.monitoring.collectorservice.model.metadata.AlertType
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface AlertRepository : MongoRepository<Alert, String> {
    
    fun findByStatus(status: AlertStatus): List<Alert>
    
    fun findByAlertType(alertType: AlertType): List<Alert>
    
    fun findByStatusAndAlertType(status: AlertStatus, alertType: AlertType): List<Alert>
    
    fun findByServiceName(serviceName: String): List<Alert>
    
    fun findByTimestampBetween(start: Instant, end: Instant): List<Alert>
    
    fun countByAlertType(alertType: AlertType): Long
    
    fun countByStatus(status: AlertStatus): Long
}