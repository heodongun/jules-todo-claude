package com.board

import com.board.database.DatabaseFactory
import com.board.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureRouting()
    configureCORS()
    configureStatusPages()
    configureMonitoring()
}
