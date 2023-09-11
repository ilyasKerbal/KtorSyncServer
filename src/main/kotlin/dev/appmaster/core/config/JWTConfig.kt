package dev.appmaster.core.config

data class JWTConfig(
    val issuer: String,
    val audience: String,
    val claim: String
)
