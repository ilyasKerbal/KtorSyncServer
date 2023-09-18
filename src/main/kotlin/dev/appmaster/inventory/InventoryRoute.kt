package dev.appmaster.inventory

import dev.appmaster.auth.domain.model.AuthPrincipal
import dev.appmaster.core.config.EndPoint
import dev.appmaster.core.config.FailureMessages
import dev.appmaster.core.domain.model.HttpResponse
import dev.appmaster.core.domain.model.generateHttpResponse
import dev.appmaster.inventory.domain.controller.InventoryController
import dev.appmaster.inventory.external.response.InventoryResponse
import dev.appmaster.inventory.utils.handleRouteWithFile
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
                handleRouteWithFile(
                    call = call
                ) { deviceId, json, filename, fileBytes ->
                    inventoryController.addInventory(
                        deviceId = deviceId,
                        requestContent = json,
                        fileName = filename,
                        imageBytes = fileBytes
                    )
                }
            }
        }
    }
}