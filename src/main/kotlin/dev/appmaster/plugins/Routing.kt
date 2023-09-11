package dev.appmaster.plugins

import dev.appmaster.auth.authRout
import dev.appmaster.core.config.EndPoint
import dev.appmaster.core.config.StatusMessages
import dev.appmaster.core.domain.model.State
import dev.appmaster.core.domain.model.StatusResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get(EndPoint.Root.path) {
            call.respond(status = HttpStatusCode.OK, message = StatusResponse(State.SUCCESS, StatusMessages.SERVER_OK))
        }

        staticResources("/content", "static")

        authRout()
    }
}
