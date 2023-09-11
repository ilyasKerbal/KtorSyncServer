package dev.appmaster.auth.data.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import dev.appmaster.core.config.JWTConfig
import dev.appmaster.core.config.SecretConfig

interface JWTController {
    val verifier: JWTVerifier

    fun sign(data: String): String
}

class JWTControllerImpl(secretConfig: SecretConfig, private val jwtConfig: JWTConfig): JWTController {

    private val algorithm = Algorithm.HMAC256(secretConfig.secretKey)

    override val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(jwtConfig.issuer)
        .withAudience(jwtConfig.audience)
        .build()

    override fun sign(data: String): String = JWT
        .create()
        .withIssuer(jwtConfig.issuer)
        .withAudience(jwtConfig.audience)
        .withClaim(jwtConfig.claim, data)
        .sign(algorithm)
}