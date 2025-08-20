package com.anshtya.jetx.server.database

import com.anshtya.jetx.server.database.entity.ProfileTable
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val dbName = environment.config.propertyOrNull("db.name")?.getString() ?: "postgres"
    val dbHost = environment.config.propertyOrNull("db.host")?.getString() ?: "localhost"
    val dbUser = environment.config.propertyOrNull("db.user")?.getString() ?: "postgres"
    val dbPassword = environment.config.propertyOrNull("db.password")?.getString() ?: "postgres"
    val db = Database.connect(
        url = "jdbc:postgresql://${dbHost}:5432/${dbName}",
        user = dbUser,
        password = dbPassword,
    )
    transaction(db) {
        SchemaUtils.create(ProfileTable)
    }
}
