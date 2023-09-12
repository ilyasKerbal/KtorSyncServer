package dev.appmaster.auth.data.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import java.util.UUID

object Devices : UUIDTable() {
    val user: Column<EntityID<UUID>> = reference("user", Users)
    val notificationId: Column<String> = text("notification_id")
    val lastSync: Column<DateTime> = datetime("last_sync").default(DateTime.now())
    val lastOpen: Column<DateTime> = datetime("last_open").default(DateTime.now())
    val deviceName: Column<String> = varchar("device_name", 50)
    val deviceBrand: Column<String> = varchar("device_brand", 50)
    val createDate: Column<DateTime> = datetime("create_date").default(DateTime.now())
}
