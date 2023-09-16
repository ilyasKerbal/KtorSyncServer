package dev.appmaster.inventory

import dev.appmaster.inventory.data.dao.ItemsDao
import dev.appmaster.inventory.data.dao.ItemsDaoImpl
import dev.appmaster.inventory.domain.controller.InventoryController
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val inventoryModule = module {
    singleOf(::ItemsDaoImpl) {
        bind<ItemsDao>()
    }

    singleOf(::InventoryController)
}