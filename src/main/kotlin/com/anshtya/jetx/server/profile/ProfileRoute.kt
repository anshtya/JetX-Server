package com.anshtya.jetx.server.profile

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.profileRoutes() {
    val profileRepository by inject<ProfileRepository>()

    routing {
        route("/profile") {
            post("/create") {
                try {
                    val profile = call.receive<Profile>()
                    profileRepository.createProfile(profile)
                    call.respond(HttpStatusCode.Created)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            get("/{username}") {
                try {
                    val username = call.parameters["username"]
                        ?: throw IllegalArgumentException("Invalid username")

                    val profile = profileRepository.getProfile(username)
                    profile?.let {
                        call.respond(HttpStatusCode.OK, profile)
                    } ?: call.respond(HttpStatusCode.NotFound)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid username")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}