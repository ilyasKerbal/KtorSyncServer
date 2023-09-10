package dev.appmaster.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class StatusResponse(override val status: State, override val message: String): Response
