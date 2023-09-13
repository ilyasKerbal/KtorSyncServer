package dev.appmaster.auth.external

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
    val deviceName: String,
    val deviceBrand: String,
    val notificationId: String
)
