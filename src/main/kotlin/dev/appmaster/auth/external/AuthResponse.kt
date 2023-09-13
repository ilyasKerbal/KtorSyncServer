package dev.appmaster.auth.external

import dev.appmaster.core.domain.model.Response
import dev.appmaster.core.domain.model.State
import kotlinx.serialization.Serializable


@Serializable
data class AuthResponse(
    override val status: State,
    override val message: String,
    val token: String? = null
) : Response {

    companion object {

        fun failed(message: String) = AuthResponse(
            State.FAILED,
            message
        )

        fun unauthorized(message: String) = AuthResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(token: String, message: String) = AuthResponse(
            State.SUCCESS,
            message,
            token
        )

        fun status(message: String) = AuthResponse(
            State.SUCCESS,
            message
        )
    }
}