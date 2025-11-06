package com.anshtya.jetx.server.firebase.service

import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MulticastMessage
import org.springframework.stereotype.Service

@Service
class FirebaseService(
    private val firebaseMessaging: FirebaseMessaging
) {
    private val androidConfig = AndroidConfig.builder()
        .setPriority(AndroidConfig.Priority.HIGH)
        .setCollapseKey("com.anshtya.jetx")
        .build()

    fun sendMessageTrigger(
        token: String
    ) {
        val message = Message.builder()
            .setToken(token)
            .setAndroidConfig(androidConfig)
            .build()

        firebaseMessaging.send(message)
    }

    fun sendMulticastMessageTrigger(
        token: List<String>
    ) {
        val message = MulticastMessage.builder()
            .addAllTokens(token)
            .setAndroidConfig(androidConfig)
            .build()

        firebaseMessaging.sendEachForMulticast(message)
    }
}