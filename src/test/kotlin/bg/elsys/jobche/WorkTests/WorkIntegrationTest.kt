package bg.elsys.jobche.WorkTests

import bg.elsys.jobche.BaseIntegrationTest
import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.entity.body.WorkBody
import bg.elsys.jobche.entity.body.application.ApplicationBody
import bg.elsys.jobche.entity.body.user.UserBody
import bg.elsys.jobche.entity.response.WorkResponse
import bg.elsys.jobche.entity.response.application.ApplicationResponse
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
        const val END_URL = "/work/"

        //User constants
        val USER_CREATOR_BODY = DefaultValues.creatorUserBody()
        val USER_CREATOR_EMAIL = USER_CREATOR_BODY.email
        val USER_CREATOR_PASSWORD = USER_CREATOR_BODY.password

        val USER_WORKER_EMAIL = "worker@worker.com"
        val USER_WORKER_PASSWORD = "worker123"
        val USER_WORKER_BODY = UserBody("Worker", "Worker", USER_WORKER_EMAIL, USER_WORKER_PASSWORD, USER_CREATOR_BODY.dateOfBirth, "0878666878")

        //Task constants
        val TASK_BODY = DefaultValues.taskBody()
    }

    var taskId: Long? = 0L
    var userCreatorId: Long? = 0L
    var userWorkerId: Long? = 0L

    @BeforeEach
    fun `register a user and create a task with the user`() {
        //Register User - creator
        userCreatorId = restTemplate.postForEntity("/users", USER_CREATOR_BODY, UserResponse::class.java).body?.id

        //Register User - worker
        userWorkerId = restTemplate.postForEntity("/users", USER_WORKER_BODY, UserResponse::class.java).body?.id

        //Create a Task and get its ID
        taskId = restTemplate
                .withBasicAuth(USER_CREATOR_EMAIL, USER_CREATOR_PASSWORD)
                .postForEntity("/tasks", TASK_BODY, TaskResponse::class.java).body?.id

        //Apply worker to Task
        val applicationId = restTemplate
                .withBasicAuth(USER_WORKER_EMAIL, USER_WORKER_PASSWORD)
                .postForEntity("/application", ApplicationBody(taskId!!), ApplicationResponse::class.java).body?.id

        //Approve worker for Task
        restTemplate
                .withBasicAuth(USER_CREATOR_EMAIL, USER_CREATOR_PASSWORD)
                .getForEntity("/application/approve/$applicationId", Unit::class.java)
    }

    @AfterEach
    fun `remove the created users`() {
        //Delete Creator
        restTemplate.withBasicAuth(USER_CREATOR_EMAIL, USER_CREATOR_PASSWORD).delete("/users")

        //Delete Worker
        restTemplate.withBasicAuth(USER_WORKER_EMAIL, USER_WORKER_PASSWORD).delete("/users")
    }

    @Nested
    inner class create {
        @Test
        fun `create work should return 201`() {
            val createResponse = createWork()

            assertThat(createResponse.statusCode).isEqualTo(HttpStatus.CREATED)

            deleteWork(createResponse.body?.id)
        }

        @Test
        fun `create work with a non accepted applier should return 405`() {
            val workBody = WorkBody(taskId!!, listOf(userCreatorId!!))
            val createResponse = restTemplate.withBasicAuth(USER_CREATOR_EMAIL, USER_CREATOR_PASSWORD).postForEntity(CREATE_URL, workBody, Unit::class.java)

            assertThat(createResponse.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        }
    }

    @Nested
    inner class delete {
        @Test
        fun `delete should return 200`() {
            val createResponse = createWork()

            val deleteResponse = restTemplate
                    .withBasicAuth(USER_CREATOR_EMAIL, USER_CREATOR_PASSWORD)
                    .exchange(DELETE_URL + createResponse.body?.id,
                            HttpMethod.DELETE,
                            HttpEntity(Unit),
                            Unit::class.java)

            assertThat(deleteResponse.statusCode).isEqualTo(HttpStatus.OK)
        }
    }

    @Nested
    inner class read {
        @Test
        fun `read should return 200`() {
            val createResponse = createWork()

            val readResponse = restTemplate
                    .withBasicAuth(USER_CREATOR_EMAIL, USER_CREATOR_PASSWORD)
                    .getForEntity(READ_URL + createResponse.body?.id, WorkResponse::class.java)

            assertThat(readResponse.statusCode).isEqualTo(HttpStatus.OK)

            deleteWork(createResponse.body?.id)
        }
    }

//    @Nested
//    inner class endWork {
//        @Test
//        fun `ending work should return 200`() {
//            val createResponse = createWork()
//
//            val endResponse = restTemplate
//                    .withBasicAuth(USER_CREATOR_EMAIL, USER_CREATOR_PASSWORD)
//                    .exchange(END_URL + createResponse.body?.id,
//                            HttpMethod.PATCH,
//                            HttpEntity(WorkStatus.ENDED),
//                            Unit::class.java)
//
//            assertThat(endResponse.statusCode).isEqualTo(HttpStatus.OK)
//
//            deleteWork(createResponse.body?.id)
//        }
//    }


    fun createWork(): ResponseEntity<WorkResponse> {
        val workBody = WorkBody(taskId!!, listOf(userWorkerId!!))
        return restTemplate.withBasicAuth(USER_CREATOR_EMAIL, USER_CREATOR_PASSWORD).postForEntity(CREATE_URL, workBody, WorkResponse::class.java)
    }

    fun deleteWork(workId: Long?) {
        restTemplate.withBasicAuth(USER_CREATOR_EMAIL, USER_CREATOR_PASSWORD).delete(DELETE_URL + workId)
    }

}