package dev.appmaster.inventory.data.dao

import dev.appmaster.auth.data.entity.EntityUser
import dev.appmaster.inventory.data.entity.EntityItem
import dev.appmaster.inventory.data.tables.Items
import dev.appmaster.inventory.domain.model.Inventory
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

interface ItemsDao {
    fun insertInventory(userID: UUID, inventory: Inventory): EntityItem

    fun getInventoryById(itemId: String): EntityItem?

    fun userCanDeleteInventory(user: EntityUser, item: EntityItem): Boolean

    fun deleteItemByID(itemId: UUID): Boolean

    fun countItemsForUser(userId: EntityID<UUID>): Long

    fun getItemsFroUser(userId: EntityID<UUID>, limit: Int, skip: Long): List<Inventory>
}

class ItemsDaoImpl : ItemsDao {

    override fun insertInventory(userID: UUID, inventory: Inventory): EntityItem = transaction {
        EntityItem.new {
            this.user = EntityUser[userID]
            this.imageTag = inventory.imageTag
            this.barcode = inventory.barCode
            this.description = inventory.description
            this.title = inventory.title
            this.lowStockAlert = inventory.lowStockAlert
            this.lowStock = inventory.lowStock
            this.expiryDateAlert = inventory.expiryDateAlert
            this.expiryDate = Inventory.fromLocalDateToJoda(inventory.expiryDate)
        }
    }

    override fun getInventoryById(itemId: String): EntityItem? = transaction {
        EntityItem.findById(UUID.fromString(itemId))
    }

    override fun userCanDeleteInventory(user: EntityUser, item: EntityItem): Boolean = transaction {
        item.user.id == user.id
    }

    override fun deleteItemByID(itemId: UUID): Boolean = transaction {
        val item = EntityItem.findById(itemId)
        item?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }

    override fun countItemsForUser(userId: EntityID<UUID>): Long = transaction {
        EntityItem.find {
            Items.user eq userId
        }.count()
    }

    override fun getItemsFroUser(userId: EntityID<UUID>, limit: Int, skip: Long): List<Inventory> = transaction {
        EntityItem.find {
            Items.user eq userId
        }.limit(n = limit, offset = skip).map { entity ->
            val item = Inventory.fromItemEntity(entity)
            item.imageTag = item.imageTag?.let { "/images/$it" }
            item
        }
    }
}