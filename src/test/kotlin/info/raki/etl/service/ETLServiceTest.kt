package info.raki.etl.service

import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.function.Consumer

class ETLServiceTest {

    @Test
    fun `should pass data from the publisher to the consumer`() {

        val consumerMock = mockk<Consumer<Int>>(relaxed = true)

        val subject = ETLService(
            flow = listOf(1, 2, 3).asFlow(),
            consumer = consumerMock,
            bufferCapacity = 100
        )

        runBlocking {
            subject.process()
            verify(exactly = 3) { consumerMock.accept(any()) }
        }
    }
}