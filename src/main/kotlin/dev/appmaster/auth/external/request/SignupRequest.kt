package dev.appmaster.auth.external.request

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val name: String,
    val email: String,
    val password: String,
    val deviceName: String,
    val deviceBrand: String,
    val notificationId: String,
)
