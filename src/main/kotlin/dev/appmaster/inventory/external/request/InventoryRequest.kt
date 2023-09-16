package dev.appmaster.inventory.external.request

import kotlinx.serialization.Serializable

@Serializable
data class InventoryRequest(
    val title: String,
    val description: String,
    val barCode: String? = null,
    val lowStockAlert: Boolean,
    val lowStock: Int? = null,
    val expiryDateAlert: Boolean,
    val expiryYear: Int? = null,
    val expiryMonth: Int? = null,
    val expiryDay: Int? = null
)