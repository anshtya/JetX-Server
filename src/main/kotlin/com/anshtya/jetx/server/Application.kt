package com.anshtya.jetx.server

import com.anshtya.jetx.server.config.configureFrameworks
import com.anshtya.jetx.server.config.configureRouting
import com.anshtya.jetx.server.config.configureSerialization
import com.anshtya.jetx.server.database.configureDatabases
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureFrameworks()
    configureSerialization()
    configureDatabases()
    configureRouting()
}
