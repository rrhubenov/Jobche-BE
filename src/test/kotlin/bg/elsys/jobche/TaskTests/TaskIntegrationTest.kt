package bg.elsys.jobche.TaskTests

import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.response.TaskResponse
import bg.elsys.jobche.entity.response.UserResponse
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import java.time.LocalDateTime


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class TaskCreateIntegrationTest {

    companion object {
        const val FIRST_NAME = "Random"
        const val LAST_NAME = "Random"
        const val EMAIL = "random@random.com"
        const val PASSWORD = "random"
        const val REGISTER_URL = "/users"
        const val BASE_URL = "/tasks/"
        const val USERS_URL = "/users/"
        const val CREATE_URL = BASE_URL
        const val READ_URL = BASE_URL
        const val UPDATE_URL = BASE_URL
        const val DELETE_URL = BASE_URL
        const val GET_ALL_URL = BASE_URL
        const val TASK_TITLE = "Test Title"
        const val TASK_PAYMENT = 1
        const val TASK_NUMBER_OF_WORKERS = 1
        const val TASK_DESCRIPTION = "Test Description"
        val TASK_TIME_OF_WORK = LocalDateTime.now()
        val taskBody = TaskBody(TASK_TITLE, TASK_PAYMENT, TASK_NUMBER_OF_WORKERS, TASK_DESCRIPTION, TASK_TIME_OF_WORK)
    }

    @Autowired
    lateinit var restTemplate: TestRestTemplate


    lateinit var registerResponse: ResponseEntity<UserResponse>

    @BeforeEach
    fun registerUser(){
        val registerUserBody = UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)
        registerResponse = restTemplate.postForEntity(REGISTER_URL, registerUserBody, UserResponse::class.java)
    }

    @AfterEach
    fun deleteUser() {
        restTemplate.delete(USERS_URL + registerResponse.body?.id)
    }

    @Nested
    inner class createTask {

        @Test
        fun `create task with user authenticated should return 201`() {
            val taskResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .postForEntity(CREATE_URL, taskBody, TaskResponse::class.java)

            assertThat(taskResponse.statusCode).isEqualTo(HttpStatus.CREATED)
        }

        @Test
        fun `create task with user unauthenticated should return 401`() {
            val taskResponse = restTemplate
                    .withBasicAuth("Invalid", "Invalid")
                    .postForEntity(CREATE_URL, taskBody, TaskResponse::class.java)

            assertThat(taskResponse.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        }

    }

    @Nested
    inner class readTask {
        @Test
        fun `read task with user authenticated should return 200`() {
            val taskResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .postForEntity(CREATE_URL, taskBody, TaskResponse::class.java)

            val getResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .getForEntity(READ_URL + taskResponse.body?.id, TaskResponse::class.java)

            Assertions.assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
        }
    }

}