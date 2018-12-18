package bg.elsys.jobche.UserTests

import bg.elsys.jobche.entity.body.UserLoginBody
import bg.elsys.jobche.entity.body.UserRegisterBody
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
        const val BASE_URL = "/users"
        const val REGISTER_URL = BASE_URL
        const val LOGIN_URL = BASE_URL + "/login"
        const val FIRST_NAME = "Radosalv"
        const val LAST_NAME = "Hubenov"
        const val EMAIL = "rrhubenov@gmail.com"
        const val PASSWORD = "testing1"
    }

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun testRegisterAndLoginUser() {
        val registerUserBody = UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)

        val registerResponse = restTemplate.postForEntity(REGISTER_URL, registerUserBody, UserResponse::class.java)
        assertThat(registerResponse.statusCode).isEqualTo(HttpStatus.CREATED)

        val loginUserBody = UserLoginBody(EMAIL, PASSWORD)

        val loginResponse = restTemplate.postForEntity(LOGIN_URL, loginUserBody, UserResponse::class.java)

        assertThat(loginResponse.statusCode).isEqualTo(HttpStatus.OK)
    }
}
