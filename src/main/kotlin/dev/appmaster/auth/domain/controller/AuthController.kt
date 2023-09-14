package dev.appmaster.auth.domain.controller

import dev.appmaster.auth.data.dao.AuthDao
import dev.appmaster.auth.data.jwt.JWTController
import dev.appmaster.auth.external.response.AuthResponse
import dev.appmaster.auth.external.request.LoginRequest
import dev.appmaster.auth.external.request.SignupRequest
import dev.appmaster.core.config.FailureMessages
import dev.appmaster.core.config.StatusMessages
import dev.appmaster.core.exception.UnauthorizedException
import io.ktor.server.plugins.*

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

    fun login(loginRequest: LoginRequest): AuthResponse = try {
        validateLoginRequest(loginRequest)

        val entityUser = authDao.getUserByEmailAndPassword(loginRequest.email, loginRequest.password) ?: throw UnauthorizedException(FailureMessages.UNAUTHORIZED_MESSAGE)
        val deviceId = authDao.addDevice(entityUser.id.value, loginRequest.notificationId, loginRequest.deviceName, loginRequest.deviceBrand)

        AuthResponse.success(token = jwtController.sign(deviceId), message = "Login successful")
    } catch (e: BadRequestException) {
        AuthResponse.failed(e.message!!)
    } catch (e: UnauthorizedException) {
        AuthResponse.unauthorized(e.message)
    } catch (e: Exception) {
        AuthResponse.failed(FailureMessages.FAILED_MESSAGE)
    }

    fun logout(deviceId: String): AuthResponse = try {
        val operation = authDao.removeDevice(deviceId)
        if (!operation) throw BadRequestException("Invalid request")
        AuthResponse.status(StatusMessages.LOGOUT_SUCCESSFUL)
    } catch (e: Exception) {
       AuthResponse.failed(e.message!!)
    }

    fun removeDevice(deviceId: String, currentDevice: String): AuthResponse = try {
        if (deviceId == currentDevice) throw BadRequestException("You cannot remove the current device, log out instead")
        if (deviceId.isBlank()) throw BadRequestException("Invalid device ID")

        val devices = authDao.getUserDeices(currentDevice)
        if (deviceId !in devices) throw UnauthorizedException("You cannot remove this device")

        val operation = authDao.removeDevice(deviceId)
        if (!operation) throw Exception("No access to device")

        AuthResponse.status(message = "Device removed successfully")
    } catch (e: UnauthorizedException) {
        AuthResponse.unauthorized(e.message)
    } catch (e: Exception) {
        AuthResponse.failed(e.message!!)
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

    private fun validateLoginRequest(loginRequest: LoginRequest) {
        val message = when {
            loginRequest.email.isBlank() -> "Email cannot be blank"
            !validateEmail(loginRequest.email) -> "Invalid email"
            loginRequest.password.isBlank() -> "Password cannot be blank"
            loginRequest.password.length in 1..5 -> "Password must be longer than 5 characters"
            loginRequest.notificationId.isBlank() -> "Invalid notification token"
            loginRequest.notificationId.length > 5000 -> "Notification token is too long"
            else -> return
        }
        throw BadRequestException(message)
    }

    private fun validateEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(emailRegex.toRegex())
    }

}