package com.anshtya.jetx.server.firebase.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MulticastMessage
import org.springframework.stereotype.Service

@Service
class FirebaseService(
    private val firebaseMessaging: FirebaseMessaging
) {
    fun sendDataMessage(
        token: String,
        data: Map<String, String>
    ) {
        val message = Message.builder()
            .putAllData(data)
            .setToken(token)
            .build()

        firebaseMessaging.send(message)
    }

    fun sendMulticastDataMessage(
        token: List<String>,
        data: Map<String, String>
    ) {
        val message = MulticastMessage.builder()
            .putAllData(data)
            .addAllTokens(token)
            .build()

        firebaseMessaging.sendEachForMulticast(message)
    }
}