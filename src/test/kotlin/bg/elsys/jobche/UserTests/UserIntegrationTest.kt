package bg.elsys.jobche.UserTests

import bg.elsys.jobche.BaseIntegrationTest
import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.response.user.UserResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class UserIntegrationTest : BaseIntegrationTest() {

    companion object {
        const val BASE_URL = "/users"
        const val REGISTER_URL = BASE_URL
        const val LOGIN_URL = BASE_URL + "/login"
        const val REMOVE_URL = BASE_URL
        const val UPDATE_URL = BASE_URL
        const val FIRST_NAME = "Radoslav"
        const val LAST_NAME = "Hubenov"
        val DATE_OF_BIRTH = DateOfBirth(1, 1, 2000)
        const val PHONE_NUM = "0878555373"
        private val registerUserBody = DefaultValues.userRegisterBody
        private val loginUserBody = DefaultValues.userLoginBody
        lateinit var registerResponse: ResponseEntity<UserResponse>
        val EMAIL = registerUserBody.email
        val PASSWORD = registerUserBody.password
    }

    @BeforeEach
    fun registerUser() {
        registerResponse = restTemplate.postForEntity(REGISTER_URL, registerUserBody, UserResponse::class.java)
    }

    @AfterEach
    fun deleteUser() {
        restTemplate
                .withBasicAuth(EMAIL, PASSWORD)
                .delete(REMOVE_URL)
    }

    @Nested
    inner class login {
        @Test
        fun `login should return 200`() {
            val loginResponse = restTemplate.postForEntity(LOGIN_URL, loginUserBody, UserResponse::class.java)

            assertThat(loginResponse.statusCode).isEqualTo(HttpStatus.OK)
        }
    }


    @Nested
    inner class create {
        @Test
        fun `create should return 201`() {
            assertThat(registerResponse.statusCode).isEqualTo(HttpStatus.CREATED)
        }

        @Test
        fun `creating a user with an already existing phone number should return 400`() {
            val invalidUserBody = UserRegisterBody(FIRST_NAME, LAST_NAME, "Random@asd.com", PASSWORD, DATE_OF_BIRTH, PHONE_NUM)
            val invalidRegisterResponse = restTemplate.postForEntity(REGISTER_URL, invalidUserBody, UserResponse::class.java)

            assertThat(invalidRegisterResponse.statusCode).isEqualTo(HttpStatus.CONFLICT)
        }

        @Test
        fun `creating a user with an already existing email should return 400`() {
            val invalidUserBody = UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, DATE_OF_BIRTH, "0878637676")
            val invalidRegisterResponse = restTemplate.postForEntity(REGISTER_URL, invalidUserBody, UserResponse::class.java)

            assertThat(invalidRegisterResponse.statusCode).isEqualTo(HttpStatus.CONFLICT)
        }
    }


    @Nested
    inner class remove {
        @Test
        fun `user removing himself should return 200 and delete resource`() {
            //Delete user and check if it exists
            val deleteResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .exchange(REMOVE_URL,
                            HttpMethod.DELETE,
                            null,
                            Unit::class.java)

            assertThat(deleteResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)

            val loginResponse = restTemplate.postForEntity(LOGIN_URL, loginUserBody, UserResponse::class.java)

            assertThat(loginResponse.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        }
    }

    @Nested
    inner class update {
        @Test
        fun `user updating himself should return 200 and update the resource`() {
            val updatedUser = UserRegisterBody(FIRST_NAME + "new", LAST_NAME, EMAIL, PASSWORD, DATE_OF_BIRTH, PHONE_NUM)

            val putResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .exchange(UPDATE_URL,
                            HttpMethod.PUT,
                            HttpEntity(updatedUser),
                            Unit::class.java)

            assertThat(putResponse.statusCode).isEqualTo(HttpStatus.OK)

            val readResponse =
                    restTemplate
                            .withBasicAuth(EMAIL, PASSWORD)
                            .getForEntity(BASE_URL + "/" + registerResponse.body?.id, UserResponse::class.java)

            assertThat(readResponse.body?.firstName).isEqualTo(FIRST_NAME + "new")
        }
    }


    @Nested
    inner class read {
        @Test
        fun `read should return 200 and a valid user response`() {
            val readResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .getForEntity(BASE_URL + "/" + registerResponse.body?.id, UserResponse::class.java)

            assertThat(readResponse.statusCode).isEqualTo(HttpStatus.OK)

            assertThat(readResponse.body).isEqualTo(registerResponse.body)
        }
    }
}
