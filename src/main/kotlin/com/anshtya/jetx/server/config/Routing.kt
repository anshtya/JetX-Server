package com.anshtya.jetx.server.config

import com.anshtya.jetx.server.profile.profileRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
    profileRoutes()
}
