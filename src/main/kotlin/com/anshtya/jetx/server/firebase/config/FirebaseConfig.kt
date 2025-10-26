package com.anshtya.jetx.server.firebase.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.InputStream

@Configuration
class FirebaseConfig {

    @Value($$"${firebase.config.path}")
    private lateinit var firebaseConfigPath: String

    @Bean
    fun firebaseMessaging(): FirebaseMessaging {
        val serviceAccount: InputStream = this::class.java
            .classLoader
            .getResourceAsStream(firebaseConfigPath)
            ?: throw IllegalArgumentException("Firebase config file not found at $firebaseConfigPath")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }

        return FirebaseMessaging.getInstance()
    }
}