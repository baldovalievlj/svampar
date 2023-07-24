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
        staticFiles("/", File("m-client/dist/m-client"))
//        singlePageApplication {
//            angular("m-client")
//        }
//        static("/static") {
//            files("m-client/dist/m-client")
//        }
//
//        // Serve the index.html file for any route that isn't handled by the server.
//        static {
//            default("m-client/dist/m-client/index.html")
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
