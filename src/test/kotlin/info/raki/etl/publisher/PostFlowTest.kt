package info.raki.etl.publisher

import info.raki.etl.api.JsonPlaceholderApi
import info.raki.etl.beans.PaginatedResponse
import info.raki.etl.beans.Post
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PostFlowTest {

    private val batchSize = 2

    private val apiMock = mockk<JsonPlaceholderApi>()

    private val subject = PostFlowSupplier(api = apiMock, batchSize = batchSize).get()

    @Test
    fun `should emit values from api`() {
        val batch1 = listOf(
            Post(userId = 1, id = 1, title = "Title", body = "Hello :)"),
            Post(userId = 1, id = 2, title = "Title2", body = "Hello2 :)")
        )
        val batch2 = listOf(
            Post(userId = 2, id = 3, title = "Title3", body = "Hello3 :)"),
        )
        runBlocking {
            coEvery { apiMock.getPosts(eq(0), eq(batchSize)) } returns
                    PaginatedResponse(batch1, 0, batchSize, 3)
            coEvery { apiMock.getPosts(eq(1), eq(batchSize)) } returns
                    PaginatedResponse(batch2, 0, batchSize, 3)

            val result = mutableListOf<Post>()
            subject.collect {
                result.add(it)
            }

            assertEquals(batch1 + batch2, result)
        }
    }
}