package dev.appmaster.auth.data.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object Users: UUIDTable() {
    val name: Column<String> = varchar("name", length = 30)
    val email: Column<String> = text("email")
    val password: Column<String> = text("password")
    val createDate: Column<DateTime> = datetime("create_date").default(DateTime.now())
}