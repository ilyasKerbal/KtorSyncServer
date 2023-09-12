package dev.appmaster.auth

import dev.appmaster.auth.domain.controller.AuthController
import dev.appmaster.auth.external.AuthResponse
import dev.appmaster.auth.external.SignupRequest
import dev.appmaster.core.config.EndPoint
import dev.appmaster.core.config.FailureMessages
import dev.appmaster.core.domain.model.State
import dev.appmaster.core.domain.model.generateHttpResponse
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authRout() {
    val authController: AuthController by inject<AuthController>()
    route(EndPoint.Signup.path) {
        post {
            val signupRequest = runCatching { call.receive<SignupRequest>() }.getOrElse {
                throw BadRequestException(FailureMessages.BAD_CREDENTIALS)
            }
            val authResponse = authController.signup(signupRequest)
            val response = authResponse.generateHttpResponse()
            call.respond(response.code, response.body as AuthResponse)
        }
    }
}