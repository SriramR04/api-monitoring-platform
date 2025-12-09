package com.monitoring.collectorservice.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

/**
 * Primary MongoDB Configuration - Logs Database
 * Stores API logs and rate limit events
 */
@Configuration
@EnableMongoRepositories(
    basePackages = ["com.monitoring.collectorservice.repository.logs"],
    mongoTemplateRef = "logsMongoTemplate"
)
class LogsMongoConfig {

    @Value("\${mongodb.logs.uri:mongodb://localhost:27017/monitoring_logs}")
    private lateinit var logsUri: String

    @Primary
    @Bean(name = ["logsMongoClient"])
    fun logsMongoClient(): MongoClient {
        return MongoClients.create(logsUri)
    }

    @Primary
    @Bean(name = ["logsMongoDatabaseFactory"])
    fun logsMongoDatabaseFactory(
        @Qualifier("logsMongoClient") mongoClient: MongoClient
    ): MongoDatabaseFactory {
        return SimpleMongoClientDatabaseFactory(mongoClient, "monitoring_logs")
    }

    @Primary
    @Bean(name = ["logsMongoTemplate"])
    fun logsMongoTemplate(
        @Qualifier("logsMongoDatabaseFactory") mongoDatabaseFactory: MongoDatabaseFactory
    ): MongoTemplate {
        return MongoTemplate(mongoDatabaseFactory)
    }
}

/**
 * Secondary MongoDB Configuration - Metadata Database
 * Stores users, alerts, and resolution tracking
 */
@Configuration
@EnableMongoRepositories(
    basePackages = ["com.monitoring.collectorservice.repository.metadata"],
    mongoTemplateRef = "metadataMongoTemplate"
)
class MetadataMongoConfig {

    @Value("\${mongodb.metadata.uri:mongodb://localhost:27017/monitoring_metadata}")
    private lateinit var metadataUri: String

    @Bean(name = ["metadataMongoClient"])
    fun metadataMongoClient(): MongoClient {
        return MongoClients.create(metadataUri)
    }

    @Bean(name = ["metadataMongoDatabaseFactory"])
    fun metadataMongoDatabaseFactory(
        @Qualifier("metadataMongoClient") mongoClient: MongoClient
    ): MongoDatabaseFactory {
        return SimpleMongoClientDatabaseFactory(mongoClient, "monitoring_metadata")
    }

    @Bean(name = ["metadataMongoTemplate"])
    fun metadataMongoTemplate(
        @Qualifier("metadataMongoDatabaseFactory") mongoDatabaseFactory: MongoDatabaseFactory
    ): MongoTemplate {
        return MongoTemplate(mongoDatabaseFactory)
    }
}