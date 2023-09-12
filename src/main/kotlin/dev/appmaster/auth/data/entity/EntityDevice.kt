package dev.appmaster.auth.data.entity

import dev.appmaster.auth.data.tables.Devices
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class EntityDevice(id: EntityID<UUID>): UUIDEntity(id) {

    companion object: UUIDEntityClass<EntityDevice>(Devices)

    var user by EntityUser referencedOn Devices.user
    var notificationId by Devices.notificationId
    var lastSync by Devices.lastSync
    var lastOpen by Devices.lastOpen
    var deviceName by Devices.deviceName
    var deviceBrand by Devices.deviceBrand
    var createDate by Devices.createDate
}