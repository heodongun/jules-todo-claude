package com.board.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

/**
 * 예외 처리를 위한 Status Pages 플러그인을 설정합니다.
 *
 * 이 함수는 `StatusPages` 플러그인을 설치하여 처리되지 않은 예외([Throwable])가 발생했을 때
 * 클라이언트에게 500 Internal Server Error 응답을 보내도록 구성합니다.
 * 또한 예외의 스택 트레이스를 서버 로그에 출력합니다.
 */
fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(
                text = "500: $cause",
                status = HttpStatusCode.InternalServerError
            )
            cause.printStackTrace()
        }
    }
}
