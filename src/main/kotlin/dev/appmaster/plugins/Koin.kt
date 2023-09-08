package dev.appmaster.plugins

import dev.appmaster.core.di.configModule
import io.ktor.server.application.*
import org.koin.core.logger.Level
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger(level = Level.ERROR)
        modules(configModule(this@configureKoin))
    }
}