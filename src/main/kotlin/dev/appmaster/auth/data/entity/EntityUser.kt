package dev.appmaster.auth.data.entity

import dev.appmaster.auth.data.tables.Users
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class EntityUser(id: EntityID<UUID>): UUIDEntity(id) {

    companion object: UUIDEntityClass<EntityUser>(Users)

    var name by Users.name
    var email by Users.email
    var password by Users.password
    var createDate by Users.createDate
}