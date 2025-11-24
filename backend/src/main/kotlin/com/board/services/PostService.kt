package com.board.services

import com.board.database.DatabaseFactory.dbQuery
import com.board.database.Posts
import com.board.models.Post
import com.board.models.CreatePostRequest
import com.board.models.UpdatePostRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

/**
 * 게시글과 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
class PostService {

    /**
     * 모든 게시글 목록을 페이지네이션하여 조회합니다.
     *
     * @param page 조회할 페이지 번호 (기본값: 1)
     * @param pageSize 한 페이지에 표시할 게시글 수 (기본값: 10)
     * @return [Post] 객체의 리스트
     */
    suspend fun getAllPosts(page: Int = 1, pageSize: Int = 10): List<Post> = dbQuery {
        Posts
            .selectAll()
            .orderBy(Posts.createdAt to SortOrder.DESC)
            .limit(pageSize, offset = ((page - 1) * pageSize).toLong())
            .map { toPost(it) }
    }

    /**
     * ID를 사용하여 특정 게시글을 조회합니다.
     *
     * @param id 조회할 게시글의 ID
     * @return 조회된 [Post] 객체, 없을 경우 null
     */
    suspend fun getPostById(id: Long): Post? = dbQuery {
        Posts
            .select { Posts.id eq id }
            .map { toPost(it) }
            .singleOrNull()
    }

    /**
     * 새로운 게시글을 생성합니다.
     *
     * @param request 게시글 생성에 필요한 데이터가 담긴 [CreatePostRequest] 객체
     * @return 생성된 [Post] 객체
     */
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

    /**
     * 기존 게시글을 수정합니다.
     *
     * @param id 수정할 게시글의 ID
     * @param request 게시글 수정에 필요한 데이터가 담긴 [UpdatePostRequest] 객체
     * @return 수정된 [Post] 객체, 수정할 게시글이 없으면 null
     */
    suspend fun updatePost(id: Long, request: UpdatePostRequest): Post? = dbQuery {
        val updated = Posts.update({ Posts.id eq id }) {
            request.title?.let { title -> it[Posts.title] = title }
            request.content?.let { content -> it[Posts.content] = content }
            it[updatedAt] = LocalDateTime.now()
        }
        if (updated > 0) getPostById(id) else null
    }

    /**
     * ID를 사용하여 특정 게시글을 삭제합니다.
     *
     * @param id 삭제할 게시글의 ID
     * @return 삭제 성공 시 true, 그렇지 않으면 false
     */
    suspend fun deletePost(id: Long): Boolean = dbQuery {
        Posts.deleteWhere { Posts.id eq id } > 0
    }

    /**
     * 전체 게시글 수를 조회합니다.
     *
     * @return 전체 게시글 수
     */
    suspend fun getTotalCount(): Int = dbQuery {
        Posts.selectAll().count().toInt()
    }

    /**
     * Exposed 라이브러리의 [ResultRow]를 [Post] 모델 객체로 변환합니다.
     *
     * @param row 데이터베이스 쿼리 결과 행
     * @return 변환된 [Post] 객체
     */
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
