package com.anshtya.jetx.server.profile

import com.anshtya.jetx.server.result.RequestResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import org.koin.ktor.ext.inject

fun Application.profileRoutes() {
    val profileService by inject<ProfileService>()

    routing {
        route("/profile") {
            post("/create") {
                try {
                    val profile = call.receive<Profile>()
                    profileService.createProfile(profile)
                    call.respond(
                        RequestResult(HttpStatusCode.Created.value, "Profile created successfully")
                    )
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    log.error("${call.route} - ${e.message}")
                    call.respond(
                        RequestResult(HttpStatusCode.InternalServerError.value, "Something went wrong")
                    )
                }
            }

            get("/{username}") {
                try {
                    val username = call.parameters["username"]
                        ?: throw IllegalArgumentException("Invalid username")

                    val profile = profileService.getProfile(username)
                    profile?.let {
                        call.respond(
                            RequestResult(HttpStatusCode.OK.value, profile)
                        )
                    } ?: call.respond(
                        RequestResult(HttpStatusCode.NotFound.value, HttpStatusCode.NotFound.description)
                    )
                } catch (e: CancellationException) {
                    throw e
                } catch (e: IllegalArgumentException) {
                    call.respond(
                        RequestResult(HttpStatusCode.BadRequest.value, e.message ?: "Invalid username")
                    )
                } catch (e: Exception) {
                    log.error("${call.route} - ${e.message}")
                    call.respond(
                        RequestResult(HttpStatusCode.InternalServerError.value, "Something went wrong")
                    )
                }
            }
        }
    }
}