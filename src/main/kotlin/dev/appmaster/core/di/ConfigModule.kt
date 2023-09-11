package dev.appmaster.core.di


import dev.appmaster.core.config.DatabaseConfig
import dev.appmaster.core.config.JWTConfig
import dev.appmaster.core.config.SecretConfig
import dev.appmaster.core.util.createConnectionString
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun configModule(application: Application) = module {
    // Application config
    single<ApplicationConfig>(createdAtStart = true){ application.environment.config }

    single<SecretConfig>(createdAtStart = true){
        val secretKey: String = get<ApplicationConfig>().config("key").property("secret").getString()
        SecretConfig(secretKey)
    }

    single<DatabaseConfig>(createdAtStart = true){
        val databaseConf = get<ApplicationConfig>().config("database")
        DatabaseConfig(
            host = databaseConf.property("host").getString(),
            port = databaseConf.property("port").getString(),
            name = databaseConf.property("name").getString(),
            user = databaseConf.property("user").getString(),
            password = databaseConf.property("password").getString(),
        )
    }

    single<CoroutineDatabase>{
        KMongo
            .createClient(connectionString = createConnectionString(get<DatabaseConfig>()))
            .getDatabase(get<DatabaseConfig>().name)
            .coroutine
    }

    single<JWTConfig>{
        val jwtConfig = get<ApplicationConfig>().config("jwt")
        JWTConfig(
            issuer = jwtConfig.property("issuer").getString(),
            audience = jwtConfig.property("audience").getString(),
            claim = jwtConfig.property("claim").getString()
        )
    }
}
