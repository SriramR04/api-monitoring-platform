package com.monitoring.collectorservice.repository.metadata

import com.monitoring.collectorservice.model.metadata.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String> {
    
    fun findByUsername(username: String): User?
    
    fun existsByUsername(username: String): Boolean
}