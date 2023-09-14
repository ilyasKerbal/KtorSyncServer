package dev.appmaster.auth.external.request

import kotlinx.serialization.Serializable

@Serializable
data class RemoveDeviceRequest(
    val deviceId: String
)
