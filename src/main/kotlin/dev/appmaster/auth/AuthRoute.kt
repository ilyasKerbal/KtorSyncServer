package dev.appmaster.auth

import dev.appmaster.auth.domain.controller.AuthController
import dev.appmaster.auth.domain.controller.ProfileController
import dev.appmaster.auth.domain.model.AuthPrincipal
import dev.appmaster.auth.external.request.LoginRequest
import dev.appmaster.auth.external.request.ProfileUpdateRequest
import dev.appmaster.auth.external.request.RemoveDeviceRequest
import dev.appmaster.auth.external.request.SignupRequest
import dev.appmaster.core.config.EndPoint
import dev.appmaster.core.config.FailureMessages
import dev.appmaster.core.domain.model.generateHttpResponse
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authRout() {
    val authController: AuthController by inject<AuthController>()
    val profileController: ProfileController by inject<ProfileController>()

    route(EndPoint.Signup.path) {
        post {
            val signupRequest = runCatching { call.receive<SignupRequest>() }.getOrElse {
                throw BadRequestException(FailureMessages.BAD_CREDENTIALS)
            }
            val authResponse = authController.signup(signupRequest)
            val response = authResponse.generateHttpResponse()
            call.respond(response.code, response.body)
        }
    }

    route(EndPoint.Login.path) {
        authenticate(strategy = AuthenticationStrategy.Optional) {
            post {
                val principal = call.authentication.principal<AuthPrincipal>()
                if (principal != null) throw BadRequestException("You cannot signup while logged in")

                val loginRequest = runCatching { call.receive<LoginRequest>() }.getOrElse {
                    throw BadRequestException(FailureMessages.BAD_CREDENTIALS)
                }
                val authResponse = authController.login(loginRequest)
                val response = authResponse.generateHttpResponse()
                call.respond(response.code, response.body)
            }
        }
    }

    route(EndPoint.Logout.path) {
        authenticate(strategy = AuthenticationStrategy.Required){
            get {
                val principal = call.authentication.principal<AuthPrincipal>() ?: throw BadRequestException(FailureMessages.BAD_CREDENTIALS)

                val authResponse = authController.logout(principal.deviceId)
                val response = authResponse.generateHttpResponse()

                call.respond(response.code, response.body)
            }
        }
    }

    route(EndPoint.RemoveDevice.path) {
        authenticate(strategy = AuthenticationStrategy.Required){
            post {
                val principal = call.authentication.principal<AuthPrincipal>() ?: throw BadRequestException(FailureMessages.BAD_CREDENTIALS)
                val removeDeviceRequest = runCatching { call.receive<RemoveDeviceRequest>() }.getOrElse {
                    throw BadRequestException(FailureMessages.BAD_CREDENTIALS)
                }
                val authResponse = authController.removeDevice(removeDeviceRequest.deviceId, principal.deviceId)
                val response = authResponse.generateHttpResponse()

                call.respond(response.code, response.body)
            }
        }
    }

    route(EndPoint.User.path) {
        authenticate {
            get {
                val principal = call.authentication.principal<AuthPrincipal>() ?: throw BadRequestException(FailureMessages.BAD_CREDENTIALS)

                val profileResponse = profileController.getProfileInfo(principal.deviceId)

                val response = profileResponse.generateHttpResponse()

                call.respond(response.code, response.body)
            }
            post {
                val principal = call.authentication.principal<AuthPrincipal>() ?: throw BadRequestException(FailureMessages.BAD_CREDENTIALS)

                val profileUpdateRequest = runCatching { call.receive<ProfileUpdateRequest>() }.getOrElse {
                    throw BadRequestException(FailureMessages.BAD_CREDENTIALS)
                }
                val profileResponse = profileController.updateProfile(principal.deviceId, profileUpdateRequest)
                val response = profileResponse.generateHttpResponse()

                call.respond(response.code, response.body)
            }
        }
    }
}