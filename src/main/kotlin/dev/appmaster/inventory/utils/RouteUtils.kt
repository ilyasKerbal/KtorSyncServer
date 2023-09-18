package dev.appmaster.inventory.utils

import dev.appmaster.auth.domain.model.AuthPrincipal
import dev.appmaster.core.config.FailureMessages
import dev.appmaster.core.domain.model.Response
import dev.appmaster.core.domain.model.generateHttpResponse
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend inline fun <reified T: Response> handleRouteWithFile(
    call: ApplicationCall,
    maxContentLength: Long = 5_000_000,
    dataField: String = "item",
    fileField: String = "itemImage",
    block: (deviceId: String, json: String?, fileName: String?, fileBytes: ByteArray?) -> T
) {
    val authPrincipal = call.authentication.principal<AuthPrincipal>() ?: throw BadRequestException(FailureMessages.UNAUTHORIZED_MESSAGE)
    val contentLength = runCatching { call.request.header(HttpHeaders.ContentLength)!!.toLong() }.getOrNull() ?: throw BadRequestException("Request size unavailable")

    if (contentLength > maxContentLength) throw BadRequestException("Request size limit is 5MB")

    val multipartData = call.receiveMultipart()
    var jsonString: String? = null
    var fileName: String? = null
    var fileBytes: ByteArray? = null

    multipartData.forEachPart { part: PartData ->
        when(part) {
            is PartData.FormItem -> {
                if (part.contentType == ContentType.Application.Json && part.name == dataField) {
                    jsonString = part.value
                }
            }
            is PartData.FileItem -> {
                if (part.name == fileField && part.contentType?.match(ContentType.Image.Any) == true) {
                    part.originalFileName?.let { fileName = it }
                    fileBytes = part.streamProvider().readBytes()
                }
            }
            else -> {}
        }
        part.dispose()
    }

    val responseObject = block(
        authPrincipal.deviceId,
        jsonString,
        fileName,
        fileBytes
    )
    val  response = responseObject.generateHttpResponse()
    call.respond(response.code, response.body)
}