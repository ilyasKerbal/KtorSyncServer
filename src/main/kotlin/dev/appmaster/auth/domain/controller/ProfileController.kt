package dev.appmaster.auth.domain.controller

import dev.appmaster.auth.data.dao.AuthDao
import dev.appmaster.auth.data.encryption.Hash
import dev.appmaster.auth.data.jwt.JWTController
import dev.appmaster.auth.domain.model.Profile
import dev.appmaster.auth.external.request.ProfileUpdateRequest
import dev.appmaster.auth.external.response.ProfileResponse
import dev.appmaster.core.config.FailureMessages
import dev.appmaster.core.exception.UnauthorizedException
import io.ktor.server.plugins.*

class ProfileController(
    private val authDao: AuthDao,
    private val hash: Hash
) {

    fun getProfileInfo(deviceId: String): ProfileResponse = executeOrCatch {
        val userEntity = authDao.getUserFromDevice(deviceId) ?: throw BadRequestException("Invalid device ID")

        val profile = Profile.fromUserEntity(userEntity)

        ProfileResponse.success(profile = profile, message = "Profile query success")
    }

    fun updateProfile(deviceId: String, request: ProfileUpdateRequest): ProfileResponse = executeOrCatch {
        var userEntity = authDao.getUserFromDevice(deviceId) ?: throw BadRequestException("Invalid device ID")
        checkNamOrThrow(request.name)

        request.password?.let {
            checkPasswordOrThrow(it)
            checkOldPassword(userEntity.password, it)
        }

        userEntity = authDao.updateUserById(userEntity.id.value, request.name, request.password)
        ProfileResponse.success(Profile.fromUserEntity(userEntity), "Update request successful")
    }

    private fun executeOrCatch(
        block: () -> ProfileResponse
    ): ProfileResponse = try {
        block()
    } catch (e: BadRequestException) {
        ProfileResponse.failed(e.message!!)
    } catch (e: UnauthorizedException) {
        ProfileResponse.unauthorized(FailureMessages.UNAUTHORIZED_MESSAGE)
    } catch (e: Exception) {
        ProfileResponse.failed(FailureMessages.FAILED_MESSAGE)
    }

    private fun checkNamOrThrow(name: String) {
        val message = when {
            name.isBlank() -> "Name cannot be blank"
            name.length > 700 -> "Name is too long"
            name.length < 2 -> "Name is too short"
            else -> return
        }
        throw BadRequestException(message = message)
    }

    private fun checkPasswordOrThrow(password: String) {
        val message = when {
            password.isBlank() -> "Password cannot be blank"
            password.length in 1..5 -> "Password must be greater than 5 characters"
            else -> return
        }
        throw BadRequestException(message = message)
    }

    private fun checkOldPassword(oldPassword: String, newPassword: String) {
        val passwordHashed = hash.hash(newPassword)
        if (passwordHashed == oldPassword) throw BadRequestException("Password is the same as the old one")
    }
}