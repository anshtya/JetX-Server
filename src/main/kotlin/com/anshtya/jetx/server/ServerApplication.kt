package com.anshtya.jetx.server

import com.anshtya.jetx.server.storage.config.B2Properties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(B2Properties::class)
class ServerApplication

fun main(args: Array<String>) {
	runApplication<ServerApplication>(*args)
}
