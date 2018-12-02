package bg.elsys.jobche.UserTests

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
        const val FIRST_NAME = "Radosalv"
        const val LAST_NAME = "Hubenov"
        const val EMAIL = "rrhubenov@gmail.com"
    }

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun testPostAndGetUser() {
        val user = User(FIRST_NAME, LAST_NAME, EMAIL)

        val postResponse = restTemplate.postForEntity(url, user, UserResponse::class.java)
        assertThat(postResponse.statusCode).isEqualTo(HttpStatus.CREATED)

        val getResponse = restTemplate.getForEntity(url + postResponse.body?.id, UserResponse::class.java)

        assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(getResponse.body).isEqualTo(postResponse.body)
    }
}
