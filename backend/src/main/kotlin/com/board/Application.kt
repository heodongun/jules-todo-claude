package com.board

import com.board.database.DatabaseFactory
import com.board.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

/**
 * 애플리케이션의 주 진입점입니다.
 * Ktor 서버 엔진을 시작합니다.
 *
 * @param args 커맨드 라인 인자
 */
fun main(args: Array<String>) {
    EngineMain.main(args)
}

/**
 * Ktor 애플리케이션의 주 모듈입니다.
 * 이 함수는 애플리케이션 시작 시 호출되며, 각종 플러그인과 설정을 초기화합니다.
 */
fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureRouting()
    configureCORS()
    configureStatusPages()
    configureMonitoring()
}
