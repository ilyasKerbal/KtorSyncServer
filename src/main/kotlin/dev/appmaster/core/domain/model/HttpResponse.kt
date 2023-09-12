package dev.appmaster.core.domain.model

import io.ktor.http.*

sealed class HttpResponse<T: Response> {
    abstract val body: T
    abstract val code: HttpStatusCode

    data class OK<T: Response>(override val body: T): HttpResponse<T>() {
        override val code: HttpStatusCode = HttpStatusCode.OK
    }

    data class NotFound<T: Response>(override val body: T): HttpResponse<T>() {
        override val code: HttpStatusCode = HttpStatusCode.NotFound
    }

    data class BadRequest<T: Response>(override val body: T): HttpResponse<T>() {
        override val code: HttpStatusCode = HttpStatusCode.BadRequest
    }

    data class Unauthorized<T: Response>(override val body: T): HttpResponse<T>() {
        override val code: HttpStatusCode = HttpStatusCode.Unauthorized
    }

    companion object {
        fun <T: Response> ok(response: T) = OK(body = response)

        fun <T: Response> notFound(response: T) = NotFound(body = response)

        fun <T: Response> badRequest(response: T) = BadRequest(body = response)

        fun <T: Response> unAuthorized(response: T) = Unauthorized(body = response)
    }

}