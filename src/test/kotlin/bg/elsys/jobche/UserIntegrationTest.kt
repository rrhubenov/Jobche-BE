package bg.elsys.jobche

import bg.elsys.jobche.entity.User
import bg.elsys.jobche.entity.response.UserResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserIntegrationTest {

    companion object {
        const val url = "/users/"
    }

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun testPostAndGetUser() {
        val user = User("Radoslav", "Hubenov")

        val postResponse = restTemplate.postForEntity(url, user, Long::class.java)
        assertThat(postResponse.statusCode).isEqualTo(HttpStatus.CREATED)

        val getResponse = restTemplate.getForEntity(url + postResponse.body, UserResponse::class.java)

        assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(getResponse.body?.id).isEqualTo(postResponse.body)
    }
}