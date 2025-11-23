package com.board.plugins

import com.board.routes.postRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Board API is running")
        }

        get("/health") {
            call.respond(mapOf("status" to "healthy"))
        }

        postRoutes()
    }
}
