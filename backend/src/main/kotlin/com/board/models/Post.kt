package com.board.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

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

@Serializable
data class CreatePostRequest(
    val title: String,
    val content: String,
    val author: String
)

@Serializable
data class UpdatePostRequest(
    val title: String? = null,
    val content: String? = null
)

@Serializable
data class PostListResponse(
    val posts: List<Post>,
    val total: Int,
    val page: Int,
    val pageSize: Int
)
