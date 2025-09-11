package com.anshtya.jetx.server.storage.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "appwrite")
data class AppwriteProperties(
    var endpoint: String = "",
    var apiKey: String = "",
    var project: String = "",
    var storageId: String = ""
)