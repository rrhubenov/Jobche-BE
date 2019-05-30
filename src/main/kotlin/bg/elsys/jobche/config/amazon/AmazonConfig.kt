package bg.elsys.jobche.config.amazon

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AmazonConfig {
    @Bean
    fun buildClient(): AmazonS3 {
        return AmazonS3ClientBuilder.defaultClient()
    }
}