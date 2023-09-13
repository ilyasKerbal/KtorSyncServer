package dev.appmaster.auth.external

import kotlinx.serialization.Serializable

@Serializable
data class RemoveDeviceRequest(
    val deviceId: String
)
