package com.board.routes

import com.board.models.CreatePostRequest
import com.board.models.PostListResponse
import com.board.models.UpdatePostRequest
import com.board.services.PostService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * 게시글과 관련된 API 엔드포인트를 정의합니다.
 *
 * 이 라우팅 확장 함수는 게시글의 생성, 조회, 수정, 삭제(CRUD) 기능을 제공하는
 * RESTful API 엔드포인트를 `/posts` 경로에 설정합니다.
 */
fun Route.postRoutes() {
    val postService = PostService()

    route("/posts") {
        /**
         * 모든 게시글 목록을 페이지네이션하여 조회합니다.
         *
         * @param page 조회할 페이지 번호 (기본값: 1)
         * @param pageSize 한 페이지에 표시할 게시글 수 (기본값: 10)
         * @return [PostListResponse] 형태의 게시글 목록과 페이지네이션 정보
         */
        get {
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10

            val posts = postService.getAllPosts(page, pageSize)
            val total = postService.getTotalCount()

            call.respond(
                HttpStatusCode.OK,
                PostListResponse(
                    posts = posts,
                    total = total,
                    page = page,
                    pageSize = pageSize
                )
            )
        }

        /**
         * ID를 사용하여 특정 게시글을 조회합니다.
         *
         * @param id 조회할 게시글의 ID
         * @return 조회된 게시글 객체
         * @throws io.ktor.server.plugins.NotFoundException 게시글을 찾을 수 없는 경우
         */
        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID"))

            val post = postService.getPostById(id)
                ?: return@get call.respond(HttpStatusCode.NotFound, mapOf("error" to "Post not found"))

            call.respond(HttpStatusCode.OK, post)
        }

        /**
         * 새로운 게시글을 생성합니다.
         *
         * @body [CreatePostRequest] 게시글 생성에 필요한 정보
         * @return 생성된 게시글 객체
         */
        post {
            val request = call.receive<CreatePostRequest>()
            val post = postService.createPost(request)
            call.respond(HttpStatusCode.Created, post)
        }

        /**
         * 기존 게시글을 수정합니다.
         *
         * @param id 수정할 게시글의 ID
         * @body [UpdatePostRequest] 게시글 수정에 필요한 정보
         * @return 수정된 게시글 객체
         * @throws io.ktor.server.plugins.NotFoundException 게시글을 찾을 수 없는 경우
         */
        put("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: return@put call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID"))

            val request = call.receive<UpdatePostRequest>()
            val post = postService.updatePost(id, request)
                ?: return@put call.respond(HttpStatusCode.NotFound, mapOf("error" to "Post not found"))

            call.respond(HttpStatusCode.OK, post)
        }

        /**
         * ID를 사용하여 특정 게시글을 삭제합니다.
         *
         * @param id 삭제할 게시글의 ID
         * @return 성공 시 204 No Content
         * @throws io.ktor.server.plugins.NotFoundException 게시글을 찾을 수 없는 경우
         */
        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID"))

            val deleted = postService.deletePost(id)
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Post not found"))
            }
        }
    }
}
