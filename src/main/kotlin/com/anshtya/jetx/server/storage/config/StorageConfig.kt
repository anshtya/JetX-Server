package com.anshtya.jetx.server.storage.config

import com.anshtya.jetx.server.storage.AppwriteClient
import io.appwrite.Client
import io.appwrite.services.Storage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class StorageConfig {
    @Bean
    fun appwriteProperties(): AppwriteProperties {
        return AppwriteProperties()
    }

    @Bean
    @Lazy
    fun appwriteClient(appwriteProperties: AppwriteProperties): AppwriteClient {
        return Client()
            .setEndpoint(appwriteProperties.endpoint)
            .setProject(appwriteProperties.project)
            .setKey(appwriteProperties.apiKey)
    }

    @Bean
    @Lazy
    fun storage(appwriteClient: AppwriteClient): Storage {
        return Storage(appwriteClient)
    }
}