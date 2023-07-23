package info.raki.etl.api

import info.raki.etl.beans.PaginatedResponse
import info.raki.etl.beans.Post
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*
import org.slf4j.LoggerFactory

class JsonPlaceholderApi(private val urlString: String) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val client = HttpClient {
        expectSuccess = true
        defaultRequest {
            url(urlString)
        }
        install(ContentNegotiation) {
            jackson()
        }
    }

    suspend fun getPosts(page: Int, limit: Int = 100): PaginatedResponse<Post> {
        logger.info("Reading posts, page: $page, limit: $limit")
        val response = client.get("/posts") {
            url {
                parameters.append("_page", "$page")
                parameters.append("_limit", "$limit")
            }
        }
        return PaginatedResponse(
            collection = response.body(),
            page = page,
            limit = limit,
            totalCount = response.headers["X-Total-Count"]?.toInt() ?: 0
        )
    }
}