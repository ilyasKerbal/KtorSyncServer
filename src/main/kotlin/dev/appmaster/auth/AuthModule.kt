package dev.appmaster.auth

import dev.appmaster.auth.data.dao.*
import dev.appmaster.auth.data.encryption.Hash
import dev.appmaster.auth.data.encryption.HashImpl
import dev.appmaster.auth.data.jwt.JWTController
import dev.appmaster.auth.data.jwt.JWTControllerImpl
import dev.appmaster.auth.domain.controller.AuthController
import dev.appmaster.core.config.JWTConfig
import dev.appmaster.core.config.SecretConfig
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun authModule() = module {

    single<Hash>{ HashImpl(get()) }

    single<JWTController>{ JWTControllerImpl(get<SecretConfig>(), get<JWTConfig>()) }

    single<AuthDao>{ AuthDaoImpl(get<Hash>()) }

    factoryOf(::AuthController)
}