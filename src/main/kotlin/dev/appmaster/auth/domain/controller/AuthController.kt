package dev.appmaster.auth.domain.controller

import dev.appmaster.auth.data.dao.AuthDao
import dev.appmaster.auth.data.jwt.JWTController
import dev.appmaster.auth.external.AuthResponse
import dev.appmaster.auth.external.SignupRequest
import dev.appmaster.core.config.FailureMessages
import io.ktor.server.plugins.*
import java.lang.Exception

class AuthController(
    private val authDao: AuthDao,
    private val jwtController: JWTController
) {

    fun signup(signupRequest: SignupRequest): AuthResponse = try {

        validateSignupRequest(signupRequest)

        if (authDao.isEmailTaken(signupRequest.email)) {
            throw BadRequestException("Email already used by another account")
        }

        val deviceId = authDao.addNewUser(signupRequest)

        val jwtToken = jwtController.sign(deviceId)

        AuthResponse.success(token = jwtToken, message = "Signup successful")
    } catch (e: BadRequestException) {
        AuthResponse.failed(e.message!!)
    } catch (e: Exception) {
        AuthResponse.failed(FailureMessages.FAILED_MESSAGE)
    }

    fun login() {

    }

    private fun validateSignupRequest(signupRequest: SignupRequest) {
        val message = when {
            signupRequest.name.isBlank() -> "Name cannot be blank"
            signupRequest.name.length > 700 -> "Name is too long"
            signupRequest.email.isBlank() -> "Email cannot be blank"
            !validateEmail(signupRequest.email) -> "Invalid email"
            signupRequest.password.isBlank() -> "Password cannot be blank"
            signupRequest.password.length in 1..5 -> "Password must be longer than 5 characters"
            signupRequest.notificationId.isBlank() -> "Invalid notification token"
            signupRequest.notificationId.length > 5000 -> "Notification token is too long"
            else -> return
        }

        throw BadRequestException(message)
    }

    private fun validateEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(emailRegex.toRegex())
    }

}