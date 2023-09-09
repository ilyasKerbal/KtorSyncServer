package dev.appmaster.core.util

import dev.appmaster.core.config.DatabaseConfig

// mongodb://username:password@localhost:27017/?authMechanism=DEFAULT
fun createConnectionString(dbConfig: DatabaseConfig): String = buildString {
    append("mongodb://")
    if (dbConfig.user.isNotBlank() && dbConfig.password.isNotBlank()){
        append("${dbConfig.user}:${dbConfig.password}@")
    }
    append("${dbConfig.host}:${dbConfig.port}/?authMechanism=DEFAULT")
}