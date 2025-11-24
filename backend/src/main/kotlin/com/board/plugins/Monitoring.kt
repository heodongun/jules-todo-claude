package com.board.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

/**
 * 로깅 및 모니터링을 설정합니다.
 *
 * 이 함수는 `CallLogging` 플러그인을 설치하여 모든 들어오는 HTTP 요청을
 * INFO 레벨로 로깅하도록 구성합니다.
 */
fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
}
