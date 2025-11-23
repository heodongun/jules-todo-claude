package com.board.services

import com.board.database.DatabaseFactory.dbQuery
import com.board.database.Posts
import com.board.models.Post
import com.board.models.CreatePostRequest
import com.board.models.UpdatePostRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class PostService {

    suspend fun getAllPosts(page: Int = 1, pageSize: Int = 10): List<Post> = dbQuery {
        Posts
            .selectAll()
            .orderBy(Posts.createdAt to SortOrder.DESC)
            .limit(pageSize, offset = ((page - 1) * pageSize).toLong())
            .map { toPost(it) }
    }

    suspend fun getPostById(id: Long): Post? = dbQuery {
        Posts
            .select { Posts.id eq id }
            .map { toPost(it) }
            .singleOrNull()
    }

    suspend fun createPost(request: CreatePostRequest): Post = dbQuery {
        val insertStatement = Posts.insert {
            it[title] = request.title
            it[content] = request.content
            it[author] = request.author
            it[createdAt] = LocalDateTime.now()
            it[updatedAt] = LocalDateTime.now()
        }
        val insertedId = insertStatement[Posts.id]
        getPostById(insertedId)!!
    }

    suspend fun updatePost(id: Long, request: UpdatePostRequest): Post? = dbQuery {
        val updated = Posts.update({ Posts.id eq id }) {
            request.title?.let { title -> it[Posts.title] = title }
            request.content?.let { content -> it[Posts.content] = content }
            it[updatedAt] = LocalDateTime.now()
        }
        if (updated > 0) getPostById(id) else null
    }

    suspend fun deletePost(id: Long): Boolean = dbQuery {
        Posts.deleteWhere { Posts.id eq id } > 0
    }

    suspend fun getTotalCount(): Int = dbQuery {
        Posts.selectAll().count().toInt()
    }

    private fun toPost(row: ResultRow): Post =
        Post(
            id = row[Posts.id],
            title = row[Posts.title],
            content = row[Posts.content],
            author = row[Posts.author],
            createdAt = row[Posts.createdAt],
            updatedAt = row[Posts.updatedAt]
        )
}
