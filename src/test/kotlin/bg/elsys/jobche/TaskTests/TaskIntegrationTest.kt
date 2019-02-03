package bg.elsys.jobche.TaskTests

import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.entity.body.task.Address
import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.model.task.PaymentType
import bg.elsys.jobche.entity.response.task.TaskPaginatedResponse
import bg.elsys.jobche.entity.response.task.TaskResponse
import bg.elsys.jobche.entity.response.user.UserResponse
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
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class TaskIntegrationTest {

    companion object {
        const val FIRST_NAME = "Random"
        const val LAST_NAME = "Random"

        const val REGISTER_URL = "/users"

        const val BASE_URL = "/tasks/"
        const val USER_DELETE_URL = "/users"
        const val CREATE_URL = BASE_URL
        const val READ_URL = BASE_URL
        const val UPDATE_URL = BASE_URL
        const val DELETE_URL = BASE_URL
        const val GET_PAGINATED = "/tasks?page=0&size=2"
        const val GET_ME_PAGINATED = "/tasks/me?page=0&size=2"

        const val TASK_TITLE = "Test Title"
        const val TASK_PAYMENT = 1
        const val TASK_NUMBER_OF_WORKERS = 1
        const val TASK_DESCRIPTION = "Test Description"
        val DATE_OF_BIRTH = DateOfBirth(1, 1, 2000)
        val TASK_TIME_OF_WORK = LocalDateTime.now()
        val TASK_LOCATION = Address("Bulgaria", "Sofia", "Krasno Selo")
        val TASK_PAYMENT_TYPE = PaymentType.BY_HOUR
        val taskBody = DefaultValues.taskBody
        val registerUserBody = DefaultValues.userRegisterBody
        val EMAIL = registerUserBody.email
        val PASSWORD = registerUserBody.password
    }

    @Autowired
    lateinit var restTemplate: TestRestTemplate


    lateinit var registerResponse: ResponseEntity<UserResponse>

    @BeforeEach
    fun registerUser() {
        registerResponse = restTemplate.postForEntity(REGISTER_URL, registerUserBody, UserResponse::class.java)
    }

    @AfterEach
    fun deleteUser() {
        restTemplate
                .withBasicAuth(EMAIL, PASSWORD)
                .delete(USER_DELETE_URL)
    }

    @Nested
    inner class create {

        @Test
        fun `create task with user authenticated should return 201`() {
            val taskResponse = createTask()

            assertThat(taskResponse.statusCode).isEqualTo(HttpStatus.CREATED)

            //Lastly Delete The Created Task
            deleteTask(taskResponse.body?.id)
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
    inner class read {
        @Test
        fun `read one task with user authenticated should return 200`() {
            val taskResponse = createTask()
            val getResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .getForEntity(READ_URL + taskResponse.body?.id, TaskResponse::class.java)

            Assertions.assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
            //Finally Delete Task
            deleteTask(taskResponse.body?.id)
        }

        @Test
        fun `read multiple tasks with user authenticated should return 200 and the tasks`() {
            //First add multiple tasks
            val taskResponse1 = createTask()
            val taskResponse2 = createTask()

            //Second, get only the first two resources
            val getResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .getForEntity(GET_PAGINATED, TaskPaginatedResponse::class.java)

            assertThat(getResponse.body?.tasks).isEqualTo(listOf(taskResponse1.body, taskResponse2.body))

            //Finally Delete the Created Tasks
            deleteTask(taskResponse1.body?.id)
            deleteTask(taskResponse2.body?.id)
        }

        @Test
        fun `list all tasks for me should return 200 and my tasks`() {
            val taskResponse = createTask()

            val getResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .getForEntity(GET_ME_PAGINATED, TaskPaginatedResponse::class.java)

            assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(getResponse.body?.tasks).isEqualTo(listOf(taskResponse.body))

            deleteTask(taskResponse.body?.id)
        }
    }

    @Nested
    inner class update {
        @Test
        fun `user updating his own task should return 200 and update task`() {
            val updatedTaskBody = TaskBody(TASK_TITLE,
                    TASK_PAYMENT,
                    TASK_NUMBER_OF_WORKERS + 1,
                    TASK_DESCRIPTION,
                    TASK_TIME_OF_WORK,
                    TASK_LOCATION,
                    TASK_PAYMENT_TYPE
            )

            val createTaskResponse = createTask()
            //Update the resource(task)
            val putResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .exchange(UPDATE_URL + createTaskResponse.body?.id,
                            HttpMethod.PUT,
                            HttpEntity(updatedTaskBody),
                            Unit::class.java)

            assertThat(putResponse.statusCode).isEqualTo(HttpStatus.OK)

            //Check that the task has been updated successfully
            val updatedTaskResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .getForEntity(READ_URL + createTaskResponse.body?.id, TaskResponse::class.java)

            assertThat(updatedTaskResponse.body?.numberOfWorkers).isEqualTo(TASK_NUMBER_OF_WORKERS + 1)

            //Delete Task After Finishing
            deleteTask(createTaskResponse.body?.id)

        }

        @Test
        fun `user updating a foreign task should return 403`() {
            val OTHER_EMAIL = "RandomEmail@abc.com"
            val OTHER_PASSWORD = "RandomPassword4"

            //Create another user that will try to update a task that does not belong to him
            val registerUserBody = UserRegisterBody(FIRST_NAME, LAST_NAME, OTHER_EMAIL, OTHER_PASSWORD, DATE_OF_BIRTH)
            registerResponse = restTemplate.postForEntity(REGISTER_URL, registerUserBody, UserResponse::class.java)

            //Create the body for the updated task
            val updatedTaskBody = TaskBody(TASK_TITLE,
                    TASK_PAYMENT,
                    TASK_NUMBER_OF_WORKERS + 1,
                    TASK_DESCRIPTION,
                    TASK_TIME_OF_WORK,
                    TASK_LOCATION,
                    TASK_PAYMENT_TYPE
            )

            //Create the task
            val createTaskResponse = createTask()

            //Try to update the task
            val putResponse = restTemplate
                    .withBasicAuth(OTHER_EMAIL, OTHER_PASSWORD)
                    .exchange(UPDATE_URL + createTaskResponse.body?.id,
                            HttpMethod.PUT,
                            HttpEntity(updatedTaskBody),
                            Unit::class.java)

            assertThat(putResponse.statusCode).isEqualTo(HttpStatus.FORBIDDEN)

            //Finally delete task and user
            deleteTask(createTaskResponse.body?.id)
            restTemplate.withBasicAuth(OTHER_EMAIL, OTHER_PASSWORD).delete(USER_DELETE_URL)

        }
    }

    @Nested
    inner class delete() {
        @Test
        fun `user deleting his own task should return 200 and delete resource`() {
            //First, create the task
            val createResponse = createTask()

            //Second, delete the task
            val deleteResponse = restTemplate
                    .withBasicAuth(EMAIL, PASSWORD)
                    .exchange(DELETE_URL + "/" + createResponse.body?.id,
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

        @Test
        fun `user deleting a foreign task should return 403`() {
            val OTHER_EMAIL = "RandomEmail@abc.com"
            val OTHER_PASSWORD = "RandomPassword4"

            //Create another user that will try to update a task that does not belong to him
            val registerUserBody = UserRegisterBody(FIRST_NAME, LAST_NAME, OTHER_EMAIL, OTHER_PASSWORD, DATE_OF_BIRTH)
            registerResponse = restTemplate.postForEntity(REGISTER_URL, registerUserBody, UserResponse::class.java)

            //Create the task
            val createTaskResponse = createTask()

            //Try to delete the task
            val deleteResponse = restTemplate
                    .withBasicAuth(OTHER_EMAIL, OTHER_PASSWORD)
                    .exchange(DELETE_URL + createTaskResponse.body?.id,
                            HttpMethod.DELETE,
                            HttpEntity(Unit),
                            Unit::class.java)

            assertThat(deleteResponse.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
            //Delete the user and task
            deleteTask(createTaskResponse.body?.id)
            restTemplate.withBasicAuth(OTHER_EMAIL, OTHER_PASSWORD).delete(USER_DELETE_URL)
        }
    }

    fun createTask(): ResponseEntity<TaskResponse> {
        return restTemplate
                .withBasicAuth(EMAIL, PASSWORD)
                .postForEntity(CREATE_URL, taskBody, TaskResponse::class.java)
    }

    fun deleteTask(id: Long?) {
        restTemplate.withBasicAuth(EMAIL, PASSWORD).delete(BASE_URL + "/" + id)
    }
}