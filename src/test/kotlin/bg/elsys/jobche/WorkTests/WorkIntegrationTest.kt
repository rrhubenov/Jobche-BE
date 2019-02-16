package bg.elsys.jobche.WorkTests

import bg.elsys.jobche.BaseIntegrationTest
import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.entity.response.WorkResponse
import bg.elsys.jobche.entity.response.task.TaskResponse
import bg.elsys.jobche.entity.response.user.UserResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class WorkIntegrationTest : BaseIntegrationTest() {

    companion object {
        //URLs
        const val CREATE_URL = "/work"
        const val READ_URL = "/work/"
        const val DELETE_URL = "/work/"
        const val UPDATE_URL = "/work/"

        //Work constants
        val workBody = DefaultValues.workBody
        val workResponse = DefaultValues.workResponse

        //User constants
        val USER_BODY = DefaultValues.userRegisterBody
        val USER_EMAIL = USER_BODY.email
        val USER_PASSWORD = USER_BODY.password

        //Task constants
        val TASK_BODY = DefaultValues.taskBody
    }

    var taskId: Long? = 0L

    @BeforeEach
    fun `register a user and create a task with the user`() {
        //Register User
        restTemplate.postForEntity("/users", USER_BODY, UserResponse::class.java)

        //Create a Task and get its ID
        taskId = restTemplate
                .withBasicAuth(USER_EMAIL, USER_PASSWORD)
                .postForEntity("/tasks", TASK_BODY, TaskResponse::class.java).body?.id
    }

    @AfterEach
    fun `remove the created user and his task`() {
        //Delete User
        restTemplate.withBasicAuth(USER_EMAIL, USER_PASSWORD).delete("/users")

        //Delete his Task
        restTemplate.withBasicAuth(USER_EMAIL, USER_PASSWORD).delete("/tasks/$taskId")
    }

    @Nested
    inner class create {
        @Test
        fun `create work should return 201`() {
            val createResponse = createWork(taskId)

            assertThat(createResponse.statusCode).isEqualTo(HttpStatus.CREATED)
        }
    }

    @Nested
    inner class delete {
        @Test
        fun `delete should return 200`() {
            val createResponse = createWork(taskId)

            val deleteResponse = restTemplate
                    .withBasicAuth(USER_EMAIL, USER_PASSWORD)
                    .exchange(DELETE_URL + createResponse.body?.id,
                            HttpMethod.DELETE,
                            HttpEntity(Unit),
                            Unit::class.java)

            assertThat(deleteResponse.statusCode).isEqualTo(HttpStatus.OK)
        }
    }


    fun createWork(taskId: Long?): ResponseEntity<WorkResponse> {
        return restTemplate.withBasicAuth(USER_EMAIL, USER_PASSWORD).postForEntity(CREATE_URL, workBody, WorkResponse::class.java)
    }

//    fun deleteWork(workId: Long?) {
//        restTemplate.withBasicAuth(USER_EMAIL, USER_PASSWORD).delete(DELETE_URL + workId)
//    }

}