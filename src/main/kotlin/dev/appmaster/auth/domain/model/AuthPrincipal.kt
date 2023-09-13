package dev.appmaster.auth.domain.model

import dev.appmaster.auth.data.entity.EntityDevice
import io.ktor.server.auth.*
import org.joda.time.DateTime

data class AuthPrincipal(
    val deviceId: String,
    val notificationId: String,
    val lastSync: DateTime,
    val lastOpen: DateTime,
    val deviceName: String,
    val deviceBrand: String,
    val createDate: DateTime
): Principal {
    companion object {
        fun fromEntity(entity: EntityDevice): AuthPrincipal = AuthPrincipal(
            deviceId = entity.id.value.toString(),
            notificationId = entity.notificationId,
            lastSync = entity.lastSync,
            lastOpen = entity.lastOpen,
            deviceName = entity.deviceName,
            deviceBrand = entity.deviceBrand,
            createDate = entity.createDate
        )
    }
}
