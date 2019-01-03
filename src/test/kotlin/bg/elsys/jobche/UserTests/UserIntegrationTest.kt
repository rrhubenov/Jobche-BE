package bg.elsys.jobche.UserTests

import bg.elsys.jobche.entity.body.user.UserLoginBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.response.UserResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
        lateinit var registerResponse : ResponseEntity<UserResponse>
    }

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @BeforeEach
    fun registerUser(){
        val registerUserBody = UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)
        registerResponse = restTemplate.postForEntity(REGISTER_URL, registerUserBody, UserResponse::class.java)
    }

    @AfterEach
    fun deleteUser() {
        restTemplate.delete(BASE_URL + "/" + registerResponse.body?.id)
    }

    @Test
    fun `login should return 200`() {
        val loginUserBody = UserLoginBody(EMAIL, PASSWORD)

        val loginResponse = restTemplate.postForEntity(LOGIN_URL, loginUserBody, UserResponse::class.java)

        assertThat(loginResponse.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `register should return 201`() {
        val registerUserBody = UserRegisterBody("Random", "Random", "Random@Random.com", "Random")
        val registerResponse = restTemplate.postForEntity(REGISTER_URL, registerUserBody, UserResponse::class.java)

        assertThat(registerResponse.statusCode).isEqualTo(HttpStatus.CREATED)
    }

    @Test
    fun `remove should delete user from database`() {
        //Delete user and check if it exists

        restTemplate.delete(BASE_URL + "/" + registerResponse.body?.id)

        val loginUserBody = UserLoginBody(EMAIL, PASSWORD)

        val loginResponse = restTemplate.postForEntity(LOGIN_URL, loginUserBody, UserResponse::class.java)

        assertThat(loginResponse.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
}
