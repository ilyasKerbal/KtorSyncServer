package dev.appmaster.inventory.external.request

import kotlinx.serialization.Serializable

@Serializable
data class InventoryDeleteRequest(
    val id: String
)
