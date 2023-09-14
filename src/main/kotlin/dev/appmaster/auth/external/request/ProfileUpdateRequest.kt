package dev.appmaster.auth.external.request

import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateRequest(
    val name: String,
    val password: String? = null
)
