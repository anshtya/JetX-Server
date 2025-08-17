package com.anshtya.jetx.server.config

import com.anshtya.jetx.server.di.AppModule
import io.ktor.server.application.*
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(AppModule().module)
    }
}
