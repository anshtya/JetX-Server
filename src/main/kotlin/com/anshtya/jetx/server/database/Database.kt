package com.anshtya.jetx.server.database

import com.anshtya.jetx.server.database.entity.ProfileTable
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils

fun Application.configureDatabases() {
    Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )
    SchemaUtils.create(ProfileTable)
}
