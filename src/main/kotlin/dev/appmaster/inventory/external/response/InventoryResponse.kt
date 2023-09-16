package dev.appmaster.inventory.external.response

import dev.appmaster.core.domain.model.Response
import dev.appmaster.core.domain.model.State
import dev.appmaster.inventory.domain.model.Inventory
import kotlinx.serialization.Serializable

@Serializable
data class InventoryResponse(
    override val status: State,
    override val message: String,
    val inventory: Inventory? = null
): Response {
    companion object {
        fun success(inventory: Inventory, message: String) = InventoryResponse(
            status = State.SUCCESS,
            message = message,
            inventory = inventory
        )

        fun failed(message: String) = InventoryResponse(
            status = State.FAILED,
            message = message
        )

        fun unauthorized(message: String) = InventoryResponse(
            status = State.UNAUTHORIZED,
            message = message
        )

        fun notfound(message: String) = InventoryResponse(
            status = State.NOT_FOUND,
            message = message
        )
    }
}
