package com.board.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * `java.time.LocalDateTime` 객체를 ISO-8601 형식의 문자열로 직렬화 및 역직렬화하는
 * `kotlinx.serialization` 라이브러리용 사용자 정의 직렬 변환기입니다.
 */
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    /**
     * [LocalDateTime] 객체를 문자열로 직렬화합니다.
     *
     * @param encoder 직렬화를 수행하는 인코더
     * @param value 직렬화할 [LocalDateTime] 객체
     */
    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    /**
     * 문자열을 [LocalDateTime] 객체로 역직렬화합니다.
     *
     * @param decoder 역직렬화를 수행하는 디코더
     * @return 역직렬화된 [LocalDateTime] 객체
     */
    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}
