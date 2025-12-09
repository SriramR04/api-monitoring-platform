package com.monitoring.collectorservice.model.metadata

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.index.Indexed

@Document(collection = "users")
data class User(
    @Id
    val id: String? = null,
    
    @Indexed(unique = true)
    val username: String,
    
    val password: String,  // BCrypt encrypted
    val email: String,
    val role: String = "DEVELOPER"
)