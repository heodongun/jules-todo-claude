package com.board.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

/**
 * 'posts' 테이블의 스키마를 정의하는 Exposed 라이브러리용 테이블 객체입니다.
 */
object Posts : Table("posts") {
    /** 게시글의 고유 ID. 기본 키입니다. */
    val id = long("id").autoIncrement()
    /** 게시글 제목 */
    val title = varchar("title", 255)
    /** 게시글 내용 */
    val content = text("content")
    /** 게시글 작성자 */
    val author = varchar("author", 100)
    /** 게시글 생성 시간 */
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    /** 게시글 수정 시간 */
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }

    override val primaryKey = PrimaryKey(id)
}
