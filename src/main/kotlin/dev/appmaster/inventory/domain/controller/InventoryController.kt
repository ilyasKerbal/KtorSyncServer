package dev.appmaster.inventory.domain.controller

import dev.appmaster.auth.data.dao.AuthDao
import dev.appmaster.core.config.FailureMessages
import dev.appmaster.core.domain.model.Response
import dev.appmaster.core.exception.UnauthorizedException
import dev.appmaster.inventory.data.dao.ItemsDao
import dev.appmaster.inventory.domain.model.Inventory
import dev.appmaster.inventory.external.request.InventoryRequest
import dev.appmaster.inventory.external.response.AllInventoryResponse
import dev.appmaster.inventory.external.response.InventoryResponse
import dev.appmaster.inventory.utils.getFileExtension
import dev.appmaster.inventory.utils.isImageExtValid
import dev.appmaster.inventory.utils.isValidImage
import io.ktor.server.plugins.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import java.io.File
import java.util.UUID
import kotlin.math.ceil

class InventoryController(
    private val itemsDao: ItemsDao,
    private val authDao: AuthDao
) {

    fun addInventory(
        deviceId: String,
        requestContent: String?,
        fileName: String?,
        imageBytes: ByteArray?
    ) : InventoryResponse = executeOrCatchInventory {
        if (requestContent.isNullOrBlank()) throw BadRequestException("Invalid inventory data")

        val userEntity = authDao.getUserFromDevice(deviceId) ?: throw UnauthorizedException("You are not authorized to add inventory")
        val inventoryRequest = runCatching { Json.decodeFromString<InventoryRequest>(requestContent) }.getOrNull() ?: throw BadRequestException("Invalid inventory data")

        if (!isImageExtValid(fileName)) throw BadRequestException("Only JPG, PNG, WebP images are supported")

        if (!isValidImage(imageBytes?.copyOfRange(0, 15))) throw BadRequestException("Invalid image, please choose another one")

        val newFileName = fileName?.let { name ->
            (UUID.randomUUID().toString() + deviceId).filter { it != '-' }.toList().shuffled().joinToString("") + ".${getFileExtension(name)}"
        }

        imageBytes?.let { bytes ->
            newFileName?.let {
                val file = File("src/main/resources/uploads/$it")
                if (!file.exists()) file.createNewFile()
                file.writeBytes(bytes)
                file.setExecutable(false, false)
            }
        }

        val inventory = Inventory.fromInventoryRequest(inventoryRequest)
        inventory.imageTag = newFileName

        val entityItem = itemsDao.insertInventory(userEntity.id.value, inventory)

        val inventoryResult = Inventory.fromItemEntity(entityItem)

        InventoryResponse.success(inventory = inventoryResult, message = "Item added successful")
    }

    fun deleteInventory(deviceId: String, itemId: String): InventoryResponse = executeOrCatchInventory {
        if(itemId.isBlank()) throw BadRequestException("Invalid item ID")
        val userEntity = authDao.getUserFromDevice(deviceId) ?: throw UnauthorizedException("You are not authorized to add inventory")

        val itemEntity = itemsDao.getInventoryById(itemId) ?: throw BadRequestException("Invalid item ID")

        if (!itemsDao.userCanDeleteInventory(userEntity, itemEntity)) throw UnauthorizedException("You cannot delete this item")

        itemEntity.imageTag?.let {
            val file = File("src/main/resources/uploads/$it")
            if (file.exists()) {
                file.delete()
            }
        }

        val result = itemsDao.deleteItemByID(itemEntity.id.value)

        if (!result) throw BadRequestException("Item deleted or unavailable")

        InventoryResponse.status("Item deleted successfully")
    }

    fun getAllInventory(deviceId: String, page: Int, pageSize: Int = 10): AllInventoryResponse = try {
        val userEntity = authDao.getUserFromDevice(deviceId) ?: throw UnauthorizedException("You are not authorized to add inventory")
        val dataSize: Long = itemsDao.countItemsForUser(userEntity.id)
        val totalPages = ((dataSize + pageSize - 1) / pageSize).coerceAtLeast(1)

        if (page > totalPages) throw BadRequestException("Invalid page number")

        val skip: Long = ((page - 1).toLong()) * pageSize

        val items: List<Inventory> = itemsDao.getItemsFroUser(
            userId = userEntity.id, limit = pageSize, skip = skip
        )

        AllInventoryResponse.success(
            message = "Query successful",
            totalPages = totalPages,
            currentPage = page,
            totalItems = dataSize,
            items = items
        )
    } catch (e: BadRequestException) {
        AllInventoryResponse.failed(e.message!!)
    } catch (e: UnauthorizedException) {
        AllInventoryResponse.unauthorized(e.message)
    } catch (e: Exception) {
        AllInventoryResponse.failed(e.message ?: e.toString())
    }

    private fun executeOrCatchInventory(
        block: () -> InventoryResponse
    ): InventoryResponse = try {
        block()
    } catch (e: BadRequestException) {
        InventoryResponse.failed(e.message!!)
    } catch (e: UnauthorizedException) {
        InventoryResponse.unauthorized(e.message)
    } catch (e: Exception) {
        InventoryResponse.failed(FailureMessages.FAILED_MESSAGE)
    }
}