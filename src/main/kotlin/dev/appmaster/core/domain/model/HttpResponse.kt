package dev.appmaster.core.domain.model

import io.ktor.http.*

sealed class HttpResponse {
    abstract val body: Response
    abstract val code: HttpStatusCode

    data class OK(override val body: Response): HttpResponse() {
        override val code: HttpStatusCode = HttpStatusCode.OK
    }

    data class NotFound(override val body: Response): HttpResponse() {
        override val code: HttpStatusCode = HttpStatusCode.NotFound
    }

    data class BadRequest(override val body: Response): HttpResponse() {
        override val code: HttpStatusCode = HttpStatusCode.BadRequest
    }

    data class Unauthorized(override val body: Response): HttpResponse() {
        override val code: HttpStatusCode = HttpStatusCode.Unauthorized
    }

    companion object {
        fun ok(response: Response) = OK(body = response)

        fun notFound(response: Response) = NotFound(body = response)

        fun badRequest(response: Response) = BadRequest(body = response)

        fun unAuthorized(response: Response) = Unauthorized(body = response)
    }

}