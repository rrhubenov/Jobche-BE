package bg.elsys.jobche.TaskTests

import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.response.TaskResponse
import bg.elsys.jobche.entity.response.UserResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class TaskIntegrationTest{

    companion object {
        const val FIRST_NAME = "Random"
        const val LAST_NAME = "Random"
        const val EMAIL = "random@random.com"
        const val PASSWORD = "random"
        const val REGISTER_UTL = "/users"
        const val BASE_URL = "/tasks"
        const val CREATE_URL = BASE_URL
        const val READ_URL = BASE_URL + "/1"
        const val UPDATE_URL = BASE_URL + "/1"
        const val DELETE_URL = BASE_URL + "/1"
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

    @Test
    fun createTaskAuthenticated() {
        //Create a valid user for authentication
        restTemplate.postForEntity(REGISTER_UTL,
                UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD),
                UserResponse::class.java)

        val taskBody = TaskBody(TASK_TITLE, TASK_PAYMENT, TASK_NUMBER_OF_WORKERS, TASK_DESCRIPTION, TASK_TIME_OF_WORK)

        val taskResponse = restTemplate
                .withBasicAuth(EMAIL, PASSWORD)
                .postForEntity(CREATE_URL, taskBody, TaskResponse::class.java)

        assertThat(taskResponse.statusCode).isEqualTo(HttpStatus.CREATED)
    }

    @Test
    fun createTaskUnauthenticated() {
        val taskResponse = restTemplate
                .withBasicAuth("Invalid", "Invalid")
                .postForEntity(CREATE_URL, taskBody, TaskResponse::class.java)

        assertThat(taskResponse.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }
}