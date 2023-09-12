package dev.appmaster.core.config

data class DatabaseConfig(
    val host: String,
    val port: String,
    val name: String,
    val user: String,
    val password: String,
    val pool: Int
)
