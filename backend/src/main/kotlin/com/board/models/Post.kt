package com.board.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * 게시글을 나타내는 데이터 클래스입니다.
 *
 * @property id 게시글의 고유 ID
 * @property title 게시글 제목
 * @property content 게시글 내용
 * @property author 게시글 작성자
 * @property createdAt 게시글 생성 시간
 * @property updatedAt 게시글 수정 시간
 */
@Serializable
data class Post(
    val id: Long? = null,
    val title: String,
    val content: String,
    val author: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

/**
 * 게시글 생성을 위한 요청 데이터를 나타내는 클래스입니다.
 *
 * @property title 게시글 제목
 * @property content 게시글 내용
 * @property author 게시글 작성자
 */
@Serializable
data class CreatePostRequest(
    val title: String,
    val content: String,
    val author: String
)

/**
 * 게시글 수정을 위한 요청 데이터를 나타내는 클래스입니다.
 *
 * @property title 수정할 게시글 제목 (선택 사항)
 * @property content 수정할 게시글 내용 (선택 사항)
 */
@Serializable
data class UpdatePostRequest(
    val title: String? = null,
    val content: String? = null
)

/**
 * 게시글 목록 조회의 응답 데이터를 나타내는 클래스입니다.
 *
 * @property posts 게시글 목록
 * @property total 전체 게시글 수
 * @property page 현재 페이지 번호
 * @property pageSize 한 페이지당 게시글 수
 */
@Serializable
data class PostListResponse(
    val posts: List<Post>,
    val total: Int,
    val page: Int,
    val pageSize: Int
)
