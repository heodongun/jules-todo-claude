package com.board

import com.board.models.CreatePostRequest
import com.board.models.PostListResponse
import com.board.models.UpdatePostRequest
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PostRoutesTest {

    @Test
    fun testHealthEndpoint() = testApplication {
        application {
            module()
        }
        client.get("/health").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testCreatePost() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val request = CreatePostRequest(
            title = "Test Post",
            content = "Test Content",
            author = "Test Author"
        )

        val response = client.post("/posts") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun testGetAllPosts() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val createRequest = CreatePostRequest(
            title = "Test Post",
            content = "Test Content",
            author = "Test Author"
        )
        client.post("/posts") {
            contentType(ContentType.Application.Json)
            setBody(createRequest)
        }

        val response = client.get("/posts")
        assertEquals(HttpStatusCode.OK, response.status)

        val postList = response.body<PostListResponse>()
        assertTrue(postList.posts.isNotEmpty())
        assertEquals(1, postList.total)
    }

    @Test
    fun testGetPostById() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val createRequest = CreatePostRequest(
            title = "Test Post",
            content = "Test Content",
            author = "Test Author"
        )
        val createResponse = client.post("/posts") {
            contentType(ContentType.Application.Json)
            setBody(createRequest)
        }
        val createdPost = createResponse.body<com.board.models.Post>()

        val response = client.get("/posts/${createdPost.id}")
        assertEquals(HttpStatusCode.OK, response.status)

        val post = response.body<com.board.models.Post>()
        assertEquals("Test Post", post.title)
    }

    @Test
    fun testUpdatePost() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val createRequest = CreatePostRequest(
            title = "Original Title",
            content = "Original Content",
            author = "Test Author"
        )
        val createResponse = client.post("/posts") {
            contentType(ContentType.Application.Json)
            setBody(createRequest)
        }
        val createdPost = createResponse.body<com.board.models.Post>()

        val updateRequest = UpdatePostRequest(
            title = "Updated Title",
            content = "Updated Content"
        )
        val updateResponse = client.put("/posts/${createdPost.id}") {
            contentType(ContentType.Application.Json)
            setBody(updateRequest)
        }

        assertEquals(HttpStatusCode.OK, updateResponse.status)

        val updatedPost = updateResponse.body<com.board.models.Post>()
        assertEquals("Updated Title", updatedPost.title)
        assertEquals("Updated Content", updatedPost.content)
    }

    @Test
    fun testDeletePost() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val createRequest = CreatePostRequest(
            title = "Test Post",
            content = "Test Content",
            author = "Test Author"
        )
        val createResponse = client.post("/posts") {
            contentType(ContentType.Application.Json)
            setBody(createRequest)
        }
        val createdPost = createResponse.body<com.board.models.Post>()

        val deleteResponse = client.delete("/posts/${createdPost.id}")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val getResponse = client.get("/posts/${createdPost.id}")
        assertEquals(HttpStatusCode.NotFound, getResponse.status)
    }

    @Test
    fun testGetNonExistentPost() = testApplication {
        application {
            module()
        }

        val response = client.get("/posts/999999")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testPagination() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        repeat(15) { index ->
            val request = CreatePostRequest(
                title = "Test Post $index",
                content = "Test Content $index",
                author = "Test Author"
            )
            client.post("/posts") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }

        val response = client.get("/posts?page=1&pageSize=10")
        assertEquals(HttpStatusCode.OK, response.status)

        val postList = response.body<PostListResponse>()
        assertEquals(10, postList.posts.size)
        assertEquals(15, postList.total)
        assertEquals(1, postList.page)
        assertEquals(10, postList.pageSize)
    }
}
