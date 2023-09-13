package dev.appmaster.plugins

import dev.appmaster.auth.data.dao.AuthDao
import dev.appmaster.auth.data.jwt.JWTController
import dev.appmaster.auth.domain.model.AuthPrincipal
import dev.appmaster.core.config.JWTConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {

    val jwtConfig: JWTConfig by inject<JWTConfig>()
    val jwtController: JWTController by inject<JWTController>()

    authentication {
        jwt {
            verifier(jwtController.verifier)
            validate { credential ->
                val deviceID = credential.payload.getClaim(jwtConfig.claim).asString()
                val deviceEntity = inject<AuthDao>().value.getDeviceById(deviceID) ?: return@validate null
                return@validate AuthPrincipal.fromEntity(deviceEntity)
            }
        }
    }
}