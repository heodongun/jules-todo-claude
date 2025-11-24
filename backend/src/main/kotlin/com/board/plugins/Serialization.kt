package com.board.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

/**
 * 콘텐츠 협상(Content Negotiation) 및 JSON 직렬화를 설정합니다.
 *
 * 이 함수는 `ContentNegotiation` 플러그인을 설치하고, `kotlinx.serialization`을 사용하여
 * JSON 형식의 요청 본문을 파싱하고 응답을 직렬화하도록 구성합니다.
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true // JSON 출력을 사람이 읽기 쉽게 형식화합니다.
            isLenient = true // 일부 형식 오류를 너그럽게 처리합니다.
            ignoreUnknownKeys = true // JSON에 알려지지 않은 키가 있어도 무시합니다.
        })
    }
}
