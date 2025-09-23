package com.anshtya.jetx.server.storage.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "backblaze.b2")
data class B2Properties(
    var accessKey: String = "",
    var secretKey: String = "",
    var bucketName: String = "",
    var region: String = "",
    var publicUrl: String = ""
)