package com.anshtya.jetx.server.database

import com.anshtya.jetx.server.database.entity.ProfileTable
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val dbName = environment.config.tryGetString("db.name") ?: "postgres"
    val dbHost = environment.config.tryGetString("db.host") ?: "localhost"
    val dbUser = environment.config.tryGetString("db.user") ?: "postgres"
    val dbPassword = environment.config.tryGetString("db.password") ?: "postgres"
    val db = Database.connect(
        url = "jdbc:postgresql://${dbHost}:5432/${dbName}",
        user = dbUser,
        password = dbPassword,
    )
    transaction(db) {
        SchemaUtils.create(ProfileTable)
    }
}
