package dev.appmaster.core.config

sealed class EndPoint(val path: String) {
    // Root
    data object Root: EndPoint(path = "/")

    data object Signup: EndPoint(path = "/signup")

    data object Login: EndPoint(path = "/login")

    data object Logout: EndPoint(path = "/logout")

    data object RemoveDevice: EndPoint(path = "/remove_device")

    data object User: EndPoint(path = "/profile")

    data object InventoryAll: EndPoint(path = "/inventory_all")

    data object InventoryAdd: EndPoint(path = "/inventory_add")

    data object InventoryRemove: EndPoint(path = "/inventory_remove")

    data object InventoryUpdate: EndPoint(path = "/inventory_update")

    data object ImageContent: EndPoint(path = "/images/{name}")
}