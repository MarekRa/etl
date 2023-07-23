package info.raki.etl.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import org.slf4j.LoggerFactory
import java.util.function.Consumer

class ETLService<T>(
    private val flow: Flow<T>,
    private val consumer: Consumer<T>,
    private val bufferCapacity: Int
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun process() {
        val flow = flow
            .buffer(capacity = bufferCapacity)
            .catch { ex -> logger.warn("exception: $ex") }

        flow.collect {
            consumer.accept(it)
        }

        flow.onCompletion { logger.info("Completed") }
    }
}
