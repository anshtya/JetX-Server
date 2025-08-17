package com.anshtya.jetx.server.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ProfileTable: IntIdTable("profile") {
    val username = varchar("username", 20).uniqueIndex()
    val name = varchar("name", 50)
    val bio = varchar("bio", 100).nullable()
}

class ProfileEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<ProfileEntity>(ProfileTable)

    var username by ProfileTable.username
    var name by ProfileTable.name
    var bio by ProfileTable.bio
}