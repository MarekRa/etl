package info.raki.etl.beans

data class PaginatedResponse<T>(
    val collection: Collection<T>,
    val page: Int,
    val limit: Int,
    val totalCount: Int
)