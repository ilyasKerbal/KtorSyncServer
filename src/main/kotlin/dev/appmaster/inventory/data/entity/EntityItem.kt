package dev.appmaster.inventory.data.entity

import dev.appmaster.auth.data.entity.EntityUser
import dev.appmaster.inventory.data.tables.Items
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class EntityItem(id: EntityID<UUID>): UUIDEntity(id) {

    companion object: UUIDEntityClass<EntityItem>(Items)

    var user by EntityUser referencedOn Items.user
    var title by Items.title
    var description by Items.description
    var imageTag by Items.imageTag
    var barcode by Items.barcode
    var lowStockAlert by Items.lowStockAlert
    var lowStock by Items.lowStock
    var expiryDateAlert by Items.expiryDateAlert
    var expiryDate by Items.expiryDate
    var createDate by Items.createDate
    var updateDate by Items.updateDate
}