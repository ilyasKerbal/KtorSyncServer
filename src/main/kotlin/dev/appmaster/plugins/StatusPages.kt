package dev.appmaster.plugins

import dev.appmaster.core.config.FailureMessages
import dev.appmaster.core.domain.model.FailureResponse
import dev.appmaster.core.domain.model.State
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            call.respond(status = HttpStatusCode.BadRequest, message = FailureResponse(State.FAILED, cause.message ?: "Request failed"))
        }

        status(HttpStatusCode.InternalServerError) { call, httpStatusCode ->
            call.respond(status = httpStatusCode, message = FailureResponse(State.FAILED, FailureMessages.FAILED_MESSAGE))
        }

        status(HttpStatusCode.Unauthorized) { call, httpStatusCode ->
            call.respond(status = httpStatusCode, message = FailureResponse(State.UNAUTHORIZED, FailureMessages.UNAUTHORIZED_MESSAGE))
        }

        status(HttpStatusCode.NotFound) { call, httpStatusCode ->
            call.respond(status = httpStatusCode, message = FailureResponse(State.NOT_FOUND, FailureMessages.NOT_FOUND_MESSAGE))
        }
    }
}