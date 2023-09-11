package dev.appmaster.auth

import dev.appmaster.core.config.EndPoint
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRout() {
    route(EndPoint.Signup.path) {
        get {
            call.respondText("Auth Signup")
        }
    }
}