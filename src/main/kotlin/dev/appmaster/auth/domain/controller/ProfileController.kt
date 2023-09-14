package dev.appmaster.auth.domain.controller

import dev.appmaster.auth.data.dao.AuthDao
import dev.appmaster.auth.data.jwt.JWTController
import dev.appmaster.auth.domain.model.Profile
import dev.appmaster.auth.external.response.AuthResponse
import dev.appmaster.auth.external.response.ProfileResponse
import dev.appmaster.core.config.FailureMessages
import dev.appmaster.core.exception.UnauthorizedException
import io.ktor.server.plugins.*

class ProfileController(
    private val authDao: AuthDao,
    private val jwtController: JWTController
) {

    fun getProfileInfo(deviceId: String): ProfileResponse = try {
        val userEntity = authDao.getUserFromDevice(deviceId) ?: throw BadRequestException("Invalid device ID")

        val profile = Profile.fromUserEntity(userEntity)

        ProfileResponse.success(profile = profile, message = "Profile query success")
    } catch (e: BadRequestException) {
        ProfileResponse.failed(FailureMessages.BAD_CREDENTIALS)
    } catch (e: UnauthorizedException) {
        ProfileResponse.unauthorized(FailureMessages.UNAUTHORIZED_MESSAGE)
    } catch (e: Exception) {
        println(e)
        ProfileResponse.failed(FailureMessages.FAILED_MESSAGE)
    }
}