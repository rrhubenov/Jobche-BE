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
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import java.time.LocalDateTime


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class TaskIntegrationTest {

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
    inner class create {

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

    @Nested
    inner class update {
        @Test
        fun `update task should return 200 and update task`() {
            //First, create a resource(task)
            val createTaskResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .postForEntity(CREATE_URL, taskBody, TaskResponse::class.java)

            val updatedTaskBody = TaskBody(TASK_TITLE,
                    TASK_PAYMENT,
                    TASK_NUMBER_OF_WORKERS + 1,
                    TASK_DESCRIPTION,
                    TASK_TIME_OF_WORK)

            //Second, update the resource(task)
            val putResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .exchange(UPDATE_URL + createTaskResponse.body?.id,
                    HttpMethod.PUT,
                    HttpEntity(updatedTaskBody),
                    Unit::class.java)

            assertThat(putResponse.statusCode).isEqualTo(HttpStatus.OK)

            //Third, check that the task has beed updated successfully
            val updatedTaskResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .getForEntity(READ_URL + createTaskResponse.body?.id, TaskResponse::class.java)

            assertThat(updatedTaskResponse.body?.numberOfWorkers).isEqualTo(TASK_NUMBER_OF_WORKERS + 1)

        }
    }

    @Nested
    inner class delete() {
        @Test
        fun `delete task should return 200 and delete resource`() {
            //First, create the task
            val createResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .postForEntity(CREATE_URL, taskBody, TaskResponse::class.java)

            //Second, delete the task
            val deleteResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .exchange(BASE_URL + "/" + createResponse.body?.id,
                            HttpMethod.DELETE,
                            null,
                            Unit::class.java)

            assertThat(deleteResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)

            //Check if the task has been deleted successfully
            val getResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .getForEntity(READ_URL + createResponse.body?.id, TaskResponse::class.java)

            assertThat(getResponse.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        }
    }

}