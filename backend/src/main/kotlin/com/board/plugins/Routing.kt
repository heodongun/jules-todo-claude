package com.board.plugins

import com.board.routes.postRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * 애플리케이션의 라우팅을 설정합니다.
 *
 * 이 함수는 기본 경로("/") 및 상태 확인 경로("/health")를 설정하고,
 * 게시글 관련 라우트([postRoutes])를 등록합니다.
 */
fun Application.configureRouting() {
    routing {
        /**
         * API가 실행 중인지 확인하는 기본 경로입니다.
         */
        get("/") {
            call.respondText("Board API is running")
        }

        /**
         * 서비스의 상태를 확인하는 상태 확인 엔드포인트입니다.
         */
        get("/health") {
            call.respond(mapOf("status" to "healthy"))
        }

        // 게시글 관련 라우트를 등록합니다.
        postRoutes()
    }
}
