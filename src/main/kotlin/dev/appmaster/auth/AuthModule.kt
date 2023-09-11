package dev.appmaster.auth

import dev.appmaster.auth.data.dao.DeviceDao
import dev.appmaster.auth.data.dao.DeviceDaoImpl
import dev.appmaster.auth.data.dao.UserDao
import dev.appmaster.auth.data.dao.UserDaoImpl
import dev.appmaster.auth.data.encryption.Hash
import dev.appmaster.auth.data.encryption.HashImpl
import dev.appmaster.auth.data.jwt.JWTController
import dev.appmaster.auth.data.jwt.JWTControllerImpl
import dev.appmaster.core.config.JWTConfig
import dev.appmaster.core.config.SecretConfig
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase

fun authModule() = module {

    single<Hash>{ HashImpl(get()) }

    single<JWTController>{ JWTControllerImpl(get<SecretConfig>(), get<JWTConfig>()) }

    single<DeviceDao>{ DeviceDaoImpl(get<CoroutineDatabase>()) }

    single<UserDao>{ UserDaoImpl(get<CoroutineDatabase>()) }
}