package dev.appmaster.core.domain.model


interface Response {
    val status: State
    val message: String
}

enum class State {
    SUCCESS, NOT_FOUND, FAILED, UNAUTHORIZED
}

fun Response.generateHttpResponse(): HttpResponse<Response> = when(this.status) {
    State.SUCCESS -> HttpResponse.ok(this)
    State.NOT_FOUND -> HttpResponse.notFound(this)
    State.FAILED -> HttpResponse.badRequest(this)
    State.UNAUTHORIZED -> HttpResponse.unAuthorized(this)
}