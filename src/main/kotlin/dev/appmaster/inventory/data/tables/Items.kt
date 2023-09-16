package dev.appmaster.inventory.data.tables

import dev.appmaster.auth.data.tables.Users
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table.Dual.default
import org.jetbrains.exposed.sql.Table.Dual.nullable
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import java.util.UUID

object Items : UUIDTable() {
    val user: Column<EntityID<UUID>> = reference("user", Users)
    val title: Column<String> = varchar("title", length = 200)
    val description: Column<String> = text("description").default("")
    val imageTag: Column<String?> = text("image").nullable()
    val barcode: Column<String?> = varchar("barcode", length = 300).nullable()
    val lowStockAlert: Column<Boolean> = bool("lowStockAlert").default(false)
    val lowStock: Column<Int?> = integer("lowStock").nullable()
    val expiryDateAlert: Column<Boolean> = bool("expiryDateAlert").default(false)
    val expiryDate = date("expiryDate").nullable()
    val createDate = datetime("createDate").clientDefault { DateTime.now() }
    val updateDate = datetime("updateDate").clientDefault { DateTime.now() }
}