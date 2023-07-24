package com.example.plugins

import com.example.routes.*
import com.example.routes.userRouting
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.get
import java.io.File

fun Application.configureRouting(
) {
    routing {
        staticFiles("/", File("m-client/dist/m-client"), index = "index.html")
//        singlePageApplication {
//            angular("m-client")
//        }
        userRouting()
        orderRouting()
        loginRouting()
        typeRouting()
        sellerRouting()
        configurationRouting()

        get("/api/") {
            call.respondText("Test")
        }
    }
}
