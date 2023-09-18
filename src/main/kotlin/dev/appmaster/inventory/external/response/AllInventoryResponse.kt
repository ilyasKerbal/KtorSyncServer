package dev.appmaster.inventory.external.response

import dev.appmaster.core.domain.model.Response
import dev.appmaster.core.domain.model.State
import dev.appmaster.inventory.domain.model.Inventory
import kotlinx.serialization.Serializable

@Serializable
data class AllInventoryResponse(
    override val status: State,
    override val message: String,
    val totalPages: Long? = null,
    val currentPage: Int? = null,
    val totalItems: Long? = null,
    val items: List<Inventory>? = null
): Response {
    companion object {

        fun success(totalPages: Long?, currentPage: Int?, totalItems: Long?, items: List<Inventory>, message: String) = AllInventoryResponse(
            status = State.SUCCESS,
            message = message,
            totalPages = totalPages,
            currentPage = currentPage,
            totalItems = totalItems,
            items = items
        )

        fun failed(message: String) = AllInventoryResponse(
            status = State.FAILED,
            message = message
        )

        fun unauthorized(message: String) = AllInventoryResponse(
            status = State.UNAUTHORIZED,
            message = message
        )

        fun notfound(message: String) = AllInventoryResponse(
            status = State.NOT_FOUND,
            message = message
        )

        fun status(message: String) = AllInventoryResponse(
            status = State.SUCCESS,
            message = message
        )
    }
}
