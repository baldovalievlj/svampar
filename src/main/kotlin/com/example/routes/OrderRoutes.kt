package com.example.routes

import com.example.dao.order.ordersRepository
import com.example.dao.seller.sellersRepository
import com.example.dao.user.usersRepository
import com.example.models.dto.OrderDTO
import com.example.models.requests.OrderRequest
import com.example.service.EmailService
import com.example.service.PdfExportService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.lang.Exception
import java.time.format.DateTimeFormatter

fun Route.orderRouting() {
    val emailService: EmailService by inject()
    val pdfExportService: PdfExportService by inject()

    authenticate("auth-jwt") {
        route("/api/order") {
            get {
                val principal = call.principal<JWTPrincipal>()!!
                val role = principal.payload.getClaim("role").asString()
                val orders = if (role == "DISTRIBUTOR") {
                    val username = principal.payload.getClaim("username").asString()
                    val user = usersRepository.findByUsername(username) ?: return@get call.respond(
                        HttpStatusCode.NotFound,
                        "user_not_found"
                    )
                    ordersRepository.findAllByUser(user)
                } else {
                    ordersRepository.findAll()
                }
                if (orders.isNotEmpty()) {
                    call.respond(orders)
                } else {
                    call.respond(HttpStatusCode.NoContent, "no_orders")
                }
            }
            get("/paged") {
                val role = call.getRole()
                val user = if (role == "DISTRIBUTOR") {
                    call.getUsername()?.let {
                        usersRepository.findByUsername(it)
                    } ?: return@get call.respond(
                        HttpStatusCode.NotFound,
                        "user_not_found"
                    )
                } else {
                    null
                }
                val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
                val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
                val userId = user?.id?.value ?: call.parameters["userId"]?.toIntOrNull()
                val startDate = call.parameters["startDate"]
                val endDate = call.parameters["endDate"]
                val orders = ordersRepository.findAllPaged(limit, offset, userId, startDate, endDate)
                call.respond(orders)
            }
            get("/{id?}") {
                val id = call.parameters["id"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "missing_id"
                )
                val order = ordersRepository.findById(id.toInt()) ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    "order_not_found"
                )
                call.respond(order)
            }
            get("/{id?}/export") {
                val id = call.parameters["id"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "missing_id"
                )
                val order = ordersRepository.findById(id.toInt()) ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    "order_not_found"
                )
                val pdf = pdfExportService.createPdf(order)
                call.respondBytes(pdf, ContentType.Application.Pdf)
            }
            post("/{id?}/email") {
                val id = call.parameters["id"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "missing_id"
                )
                val order = ordersRepository.findById(id.toInt()) ?: return@post call.respond(
                    HttpStatusCode.NotFound,
                    "order_not_found"
                )
                val pdf = pdfExportService.createPdf(order)
                val fileName = "Order-${order.id}.pdf"
                val date = order.dateCreated.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                try {
                    val emailResponse = emailService.sendEmail(order.seller.email, pdf, fileName, date)
                    if (emailResponse.status == HttpStatusCode.OK) {
                        call.respond(HttpStatusCode.OK, "Email sent successfully.")
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "Failed to send email.")
                    }
                } catch (exception: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        "Error occurred while sending email: ${exception.message}"
                    )
                }
            }
            post {
                val order = call.receive<OrderRequest>()
                val username = call.getUsername() ?: return@post call.respond(HttpStatusCode.Unauthorized)
                val user = usersRepository.findByUsername(username) ?: return@post call.respond(
                    HttpStatusCode.NotFound,
                    "user_not_found"
                )
                val seller = sellersRepository.findById(order.seller) ?: return@post call.respond(
                    HttpStatusCode.NotFound,
                    "seller_not_found"
                )
                try {
                    ordersRepository.create(order, user, seller)
                    call.respond(HttpStatusCode.Created)
                } catch (e: Exception) {
                    call.respond(status = HttpStatusCode.Conflict, "failed_to_create_order")
                }
            }
        }
//            get("/{id?}/total") {
//                val id = call.parameters["id"] ?: return@get call.respondText(
//                    "Missing id",
//                    status = HttpStatusCode.BadRequest
//                )
//                val total = ordersRepository.findTotalForOrder(id.toInt()) ?: return@get call.respondText(
//                    "No order with id $id",
//                    status = HttpStatusCode.NotFound
//                )
//                call.respond(total)
//            }

    }


}

fun ApplicationCall.getUsername(): String? = this.principal<JWTPrincipal>()?.payload?.getClaim("username")?.asString()
fun ApplicationCall.getRole(): String? = this.principal<JWTPrincipal>()?.payload?.getClaim("role")?.asString()
