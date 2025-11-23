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

fun Route.postRoutes() {
    val postService = PostService()

    route("/posts") {
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

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID"))

            val post = postService.getPostById(id)
                ?: return@get call.respond(HttpStatusCode.NotFound, mapOf("error" to "Post not found"))

            call.respond(HttpStatusCode.OK, post)
        }

        post {
            val request = call.receive<CreatePostRequest>()
            val post = postService.createPost(request)
            call.respond(HttpStatusCode.Created, post)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: return@put call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID"))

            val request = call.receive<UpdatePostRequest>()
            val post = postService.updatePost(id, request)
                ?: return@put call.respond(HttpStatusCode.NotFound, mapOf("error" to "Post not found"))

            call.respond(HttpStatusCode.OK, post)
        }

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
