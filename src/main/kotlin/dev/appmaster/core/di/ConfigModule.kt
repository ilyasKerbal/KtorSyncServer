package dev.appmaster.core.di


import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.appmaster.auth.data.tables.Devices
import dev.appmaster.auth.data.tables.Users
import dev.appmaster.core.config.DatabaseConfig
import dev.appmaster.core.config.JWTConfig
import dev.appmaster.core.config.SecretConfig
import dev.appmaster.inventory.data.tables.Items
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module
import javax.sql.DataSource

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
            pool = databaseConf.property("pool").getString().toInt()
        )
    }

    single<DataSource>(createdAtStart = true){
        val databaseConfig = get<DatabaseConfig>()
        val config = HikariConfig()
        with(databaseConfig) {
            config.password = password
            config.jdbcUrl = "jdbc:postgresql://$host:$port/$name"
            config.maximumPoolSize = pool
            config.username = user
        }
        config.validate()
        HikariDataSource(config)
    }

    single<Database>(createdAtStart = true){
        val tables = arrayOf<UUIDTable>(Users, Devices, Items)
        val database = Database.connect(get<DataSource>())
        transaction {
            SchemaUtils.createMissingTablesAndColumns(*tables)
        }
        database
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
