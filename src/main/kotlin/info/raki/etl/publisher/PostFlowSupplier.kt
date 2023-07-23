package info.raki.etl.publisher

import info.raki.etl.api.JsonPlaceholderApi
import info.raki.etl.beans.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.function.Supplier

class PostFlowSupplier(
    private val api: JsonPlaceholderApi,
    private val batchSize: Int
) : Supplier<Flow<Post>> {

    override fun get(): Flow<Post> = flow {
        var page = 0
        do {
            val response = api.getPosts(page = page++, limit = batchSize)
            response.collection.forEach { emit(it) }
        } while (response.collection.isNotEmpty() && response.totalCount > page * batchSize)
    }
}