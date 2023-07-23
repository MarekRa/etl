package info.raki.etl.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import info.raki.etl.beans.Post
import org.slf4j.LoggerFactory
import java.io.File
import java.util.function.Consumer

class PostConsumer(
    private val outputPath: String,
    private val mapper: ObjectMapper
) : Consumer<Post> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun accept(post: Post) {
        val directory = File(outputPath)
        directory.mkdirs()
        val file = File(directory, "${post.id}.json")
        logger.info("Writing file ${file.absolutePath}")
        file.writeText(mapper.writeValueAsString(post))
    }
}