package dev.appmaster.core.config

sealed class EndPoint(val path: String) {
    // Root
    data object Root: EndPoint(path = "/")

    data object Signup: EndPoint(path = "/signup")

    data object Login: EndPoint(path = "/login")

    data object Logout: EndPoint(path = "/logout")

    data object Check: EndPoint(path = "/check")

    data object Content: EndPoint(path = "/content")
}