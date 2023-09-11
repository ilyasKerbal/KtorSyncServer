package dev.appmaster.auth.domain.model

import io.ktor.server.auth.*

data class AuthPrincipal(val device: Device, val user: User): Principal
