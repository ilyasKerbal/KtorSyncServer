package dev.appmaster.inventory

import dev.appmaster.auth.domain.model.AuthPrincipal
import dev.appmaster.core.config.EndPoint
import dev.appmaster.core.config.FailureMessages
import dev.appmaster.core.domain.model.HttpResponse
import dev.appmaster.core.domain.model.generateHttpResponse
import dev.appmaster.inventory.domain.controller.InventoryController
import dev.appmaster.inventory.external.response.InventoryResponse
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Route.inventoryRoute() {

    val inventoryController: InventoryController by inject<InventoryController>()

    route(EndPoint.ImageContent.path) {
        get {
            val filename =  call.parameters["name"] ?: throw BadRequestException("You cannot access to images root folder")
            val file = File("src/main/resources/uploads/$filename")
            if (file.exists()) {
                call.respondFile(file)
            }
            val response = InventoryResponse.notfound(FailureMessages.NOT_FOUND_MESSAGE).generateHttpResponse()

            call.respond(response.code, response.body)
        }
    }

    route(EndPoint.InventoryAdd.path) {
        authenticate {
            post {
                val authPrincipal = call.authentication.principal<AuthPrincipal>() ?: throw BadRequestException(FailureMessages.UNAUTHORIZED_MESSAGE)
                val contentLength = runCatching { call.request.header(HttpHeaders.ContentLength)!!.toLong() }.getOrNull() ?: throw BadRequestException("Request size unavailable")
                if (contentLength > 5_000_000) throw BadRequestException("Request size limit is 5MB")

                val multipartData = call.receiveMultipart()
                var jsonString = ""
                var fileName: String? = null
                var imageBytes: ByteArray? = null
                multipartData.forEachPart { part: PartData ->
                    when(part) {
                        is PartData.FormItem -> {
                            if (part.contentType == ContentType.Application.Json && part.name == "item") {
                                jsonString = part.value
                            }
                        }
                        is PartData.FileItem -> {
                            if (part.name == "itemImage" && part.contentType?.match(ContentType.Image.Any) == true) {
                                part.originalFileName?.let { fileName = it }
                                imageBytes = part.streamProvider().readBytes()
                            }
                        }
                        else -> {}
                    }
                    part.dispose()
                }
                val inventoryResponse = inventoryController.addInventory(
                    deviceId = authPrincipal.deviceId,
                    requestContent = jsonString,
                    fileName = fileName,
                    imageBytes = imageBytes
                )
                val response = inventoryResponse.generateHttpResponse()
                call.respond(response.code, response.body)
            }
        }
    }
}