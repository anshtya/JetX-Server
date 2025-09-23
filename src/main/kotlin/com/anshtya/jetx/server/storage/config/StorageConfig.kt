package com.anshtya.jetx.server.storage.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI

@Configuration
class StorageConfig(private val b2Properties: B2Properties) {
    @Bean
    @Lazy
    fun s3Client(): S3Client {
        val credentials = AwsBasicCredentials.create(
            b2Properties.accessKey,
            b2Properties.secretKey
        )

        return S3Client.builder()
            .region(Region.of(b2Properties.region))
            .endpointOverride(URI.create(b2Properties.publicUrl))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()
    }

    @Bean
    fun s3Presigner(): S3Presigner {
        val credentials = AwsBasicCredentials.create(
            b2Properties.accessKey,
            b2Properties.secretKey
        )

        return S3Presigner.builder()
            .region(Region.of(b2Properties.region))
            .endpointOverride(URI.create(b2Properties.publicUrl))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()
    }
}