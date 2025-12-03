package com.anshtya.jetx.server.firebase.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*


@Configuration
class FirebaseConfig {

    @Value($$"${firebase.config.base64}")
    private lateinit var firebaseConfigBase64: String

    @Bean
    fun firebaseMessaging(): FirebaseMessaging {
        val decodedBytes: ByteArray = Base64.getDecoder().decode(firebaseConfigBase64)
        val credentialsStream: InputStream = ByteArrayInputStream(decodedBytes)

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(credentialsStream))
            .build()

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }

        return FirebaseMessaging.getInstance()
    }
}