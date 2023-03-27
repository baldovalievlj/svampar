package com.example.routes

import com.example.dao.order.ordersRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.orderRouting() {
    authenticate("auth-jwt") {
        route("/order") {
            get() {
                val orders = ordersRepository.findAllOrders()
                if (orders.isNotEmpty()) {
                    call.respond(orders)
                } else {
                    call.respondText("No orders created yet.", status = HttpStatusCode.OK)
                }
            }
            get("/{id?}") {
                val id = call.parameters["id"] ?: return@get call.respondText(
                    "Missing id",
                    status = HttpStatusCode.BadRequest
                )
                val order = ordersRepository.findById(id.toInt()) ?: return@get call.respondText(
                    "No order with id $id",
                    status = HttpStatusCode.NotFound
                )
                call.respond(order)
            }

            get("/{id?}/total") {
                val id = call.parameters["id"] ?: return@get call.respondText(
                    "Missing id",
                    status = HttpStatusCode.BadRequest
                )
                val total = ordersRepository.findTotalForOrder(id.toInt()) ?: return@get call.respondText(
                    "No order with id $id",
                    status = HttpStatusCode.NotFound
                )
                call.respond(total)
            }
        }
    }
}