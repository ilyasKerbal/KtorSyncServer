package dev.appmaster.auth.domain.model

import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.LocalDateTime

data class Device (
    val _id: Id<Device> = newId(),
    val userId: Id<User>,
    val notificationId: String,
    val lastSync: LocalDateTime,
    val lastOpen: LocalDateTime,
    val deviceName: String,
    val deviceBrand: String,
    val createDate: LocalDateTime = LocalDateTime.now()
)
