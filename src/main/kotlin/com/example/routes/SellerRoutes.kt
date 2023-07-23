package com.example.routes

import com.example.dao.seller.sellersRepository
import com.example.dao.type.typesRepository
import com.example.models.requests.SellerRequest
import com.example.models.requests.TypeRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.lang.Exception

fun Route.sellerRouting() {
    authenticate("auth-jwt") {
        route("/api/seller") {
            get {
                val sellers = sellersRepository.findAll()
                if (sellers.isNotEmpty()) {
                    call.respond(sellers)
                } else {
                    call.respond(HttpStatusCode.NoContent, "no_content")
                }
            }
            get("/paged") {
                val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
                val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
                val sellers = sellersRepository.findAllPaged(limit, offset)
                if (sellers.totalCount > 0) {
                    call.respond(sellers)
                } else {
                    call.respond(HttpStatusCode.NoContent, "no_content")
                }
            }
            get("/{id?}") {
                val id = call.parameters["id"] ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    "missing_id"
                )
                val seller = sellersRepository.findById(id.toInt()) ?: return@get call.respond(
                    status = HttpStatusCode.NotFound,
                    "seller_not_found",
                )
                call.respond(seller.toSellerDTO())
            }
            post {
                val seller = call.receive<SellerRequest>()
                try {
                    val seller = sellersRepository.create(seller)
                    call.respond(HttpStatusCode.Created, seller.toSellerDTO())
                } catch (e: Exception) {
                    call.respond(status = HttpStatusCode.Conflict, "failed_to_create_seller")
                }
            }
            put("/{id?}") {
                val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                val request = call.receive<SellerRequest>()
                val seller = sellersRepository.findById(id.toInt()) ?: return@put call.respond(
                    status = HttpStatusCode.NotFound,
                    "No seller with id $id",
                )
                try {
                    sellersRepository.update(seller, request)
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception) {
                    call.respond(status = HttpStatusCode.Conflict, "failed_to_update_seller")
                }
            }
            delete("/{id?}") {
                val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                if (sellersRepository.deleteById(id.toInt())) {
                    call.response.status(HttpStatusCode.Accepted)
                } else {
                    call.respondText("seller_not_found", status = HttpStatusCode.NotFound)
                }
            }
        }
    }
}
