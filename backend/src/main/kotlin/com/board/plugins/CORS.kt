package com.board.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

/**
 * CORS(Cross-Origin Resource Sharing) 설정을 구성합니다.
 *
 * 이 함수는 `CORS` 플러그인을 설치하여 다른 도메인에서의 API 요청을 허용하도록 설정합니다.
 * 특정 HTTP 메서드와 헤더를 허용하고, 모든 호스트에서의 요청을 허용합니다.
 */
fun Application.configureCORS() {
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        anyHost()
    }
}
