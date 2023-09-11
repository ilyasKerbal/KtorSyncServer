package dev.appmaster.plugins

import dev.appmaster.auth.data.dao.DeviceDao
import dev.appmaster.auth.data.dao.UserDao
import dev.appmaster.auth.data.jwt.JWTController
import dev.appmaster.auth.domain.model.AuthPrincipal
import dev.appmaster.auth.domain.model.Device
import dev.appmaster.auth.domain.model.User
import dev.appmaster.core.config.JWTConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {

    val jwtConfig: JWTConfig by inject<JWTConfig>()
    val jwtController: JWTController by inject<JWTController>()

    authentication {
        jwt {
            verifier(jwtController.verifier)
            validate { credential ->
                val deviceID = credential.payload.getClaim(jwtConfig.claim).asString()
                val device: Device = get<DeviceDao>().getDeviceFromId(deviceID) ?: return@validate null
                val user: User = get<UserDao>().getUserByID(device.userId.toString()) ?: return@validate null
                return@validate AuthPrincipal(device = device, user = user)
            }
        }
    }
}