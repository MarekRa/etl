package info.raki.etl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import info.raki.etl.api.JsonPlaceholderApi
import info.raki.etl.consumer.PostConsumer
import info.raki.etl.publisher.PostFlowSupplier
import info.raki.etl.service.ETLService
import kotlinx.coroutines.runBlocking

/**
 * Environment variables:
 *
 *  URL=https://jsonplaceholder.typicode.com
 *  BATCH_SIZE=100
 *  BUFFER_CAPACITY=1000
 *  OUTPUT_PATH=out
 */
fun main() = runBlocking {
    val url = System.getenv("URL") ?: throw IllegalArgumentException("Missing URL environment variable")
    val batchSize = System.getenv("BATCH_SIZE")?.toInt() ?: 100
    val bufferCapacity = System.getenv("BUFFER_CAPACITY")?.toInt() ?: 1000
    val outputPath = System.getenv("OUTPUT_PATH") ?: "out"

    val etl = ETLService(
        flow = PostFlowSupplier(api = JsonPlaceholderApi(url), batchSize = batchSize).get(),
        consumer = PostConsumer(mapper = jacksonObjectMapper(), outputPath = outputPath),
        bufferCapacity = bufferCapacity
    )
    etl.process()
}
