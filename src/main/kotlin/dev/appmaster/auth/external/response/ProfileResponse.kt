package dev.appmaster.auth.external.response

import dev.appmaster.auth.domain.model.Profile
import dev.appmaster.core.domain.model.Response
import dev.appmaster.core.domain.model.State
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    override val status: State,
    override val message: String,
    val profile: Profile? = null
): Response {

    companion object {
        fun success(profile: Profile, message: String) = ProfileResponse(
            status = State.SUCCESS,
            message = message,
            profile = profile
        )

        fun failed(message: String) = ProfileResponse(
            status = State.FAILED,
            message = message
        )

        fun unauthorized(message: String) = ProfileResponse(
            status = State.UNAUTHORIZED,
            message = message
        )
    }
}