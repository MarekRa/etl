package info.raki.etl.consumer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import info.raki.etl.beans.Post
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class PostConsumerTest {

    private val mapper = jacksonObjectMapper()

    @Test
    fun `should save files`(@TempDir tempDir: Path) {
        val outputPath = tempDir.absolutePathString()
        val post = Post(userId = 1, id = 321, title = "Title", body = "Hello :)")
        val subject = PostConsumer(outputPath = outputPath, mapper = mapper)
        subject.accept(Post(userId = 1, id = 321, title = "Title", body = "Hello :)"))
        val result = File("${outputPath}/321.json")
        assertTrue { result.exists() }
        assertEquals(post, mapper.readValue(result, Post::class.java))
    }
}