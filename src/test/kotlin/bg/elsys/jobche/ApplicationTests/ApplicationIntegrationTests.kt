package bg.elsys.jobche.ApplicationTests

import bg.elsys.jobche.BaseIntegrationTest
import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.converter.Converters
import bg.elsys.jobche.entity.body.application.ApplicationBody
import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.model.User
import bg.elsys.jobche.entity.response.application.ApplicationPaginatedResponse
import bg.elsys.jobche.entity.response.application.ApplicationResponse
import bg.elsys.jobche.entity.response.task.TaskResponse
import bg.elsys.jobche.entity.response.user.UserResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension

class ApplicationIntegrationTests: BaseIntegrationTest() {
    companion object {
        //User constants
        const val REGISTER_URL = "/users"
        const val USER_DELETE_URL = "/users"
        val user = DefaultValues.user
        val userApplicant = User("Applicant", "Applicant", "applicant@app.com", "randompass", DateOfBirth(2, 3, 2001).toString(), "0878900955")
        //Task constants
        const val TASK_URL = "/tasks"
        const val CREATE_TASK_URL = TASK_URL
        //Application constants
        const val BASE_APPLICATION_URL = "/application"
        const val CREATE_APPLICATION_URL = BASE_APPLICATION_URL
        const val APPROVE_APPLICATION_URL = BASE_APPLICATION_URL + "/approve/"
    }

    lateinit var userCreatorResponse: ResponseEntity<UserResponse>
    lateinit var userApplicantResponse: ResponseEntity<UserResponse>
    lateinit var taskResponse: ResponseEntity<TaskResponse>
    var userCreatorId: Long? = 0
    var applicantId: Long? = 0
    var taskId: Long? = 0

    @BeforeEach
    fun setup() {
        //Create a user that will create the task
        val userCreatorBody = DefaultValues.userRegisterBody
        userCreatorResponse = restTemplate.postForEntity(REGISTER_URL, userCreatorBody, UserResponse::class.java)
        userCreatorId = userCreatorResponse.body?.id


        //Create the user that will apply for the task
        val userApplicantBody = UserRegisterBody(userApplicant.firstName, userApplicant.lastName, userApplicant.email, userApplicant.password, Converters().toDateOfBirth(userApplicant.dateOfBirth), userApplicant.phoneNum)
        userApplicantResponse = restTemplate.postForEntity(REGISTER_URL, userApplicantBody, UserResponse::class.java)
        applicantId = userApplicantResponse.body?.id

        //Create the task
        val taskBody = DefaultValues.taskBody
        taskResponse = restTemplate
                .withBasicAuth(user.email, user.password)
                .postForEntity(CREATE_TASK_URL, taskBody, TaskResponse::class.java)
        taskId = taskResponse.body?.id

    }

    @AfterEach
    fun cleanUp() {
        restTemplate
                .withBasicAuth(user.email, user.password)
                .delete("/tasks/" + taskId)

        deleteUser(user.email, user.password)
        deleteUser(userApplicant.email, userApplicant.password)

        System.out.println("asd")

    }

    @Nested
    inner class create {
        @Test
        fun `create application should return 201 and the expected response`() {
            val applicationResponse = createApplication()

            val expectedResponse = ApplicationResponse(applicationResponse.body?.id, userApplicantResponse.body, taskResponse.body, false)

            assertThat(applicationResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(applicationResponse.body).isEqualTo(expectedResponse)

            deleteApplication(applicationResponse.body?.id)
        }
    }


    @Nested
    inner class delete {
        @Test
        fun `delete application should return 204`() {
            //First, create the application
            val createResponse = createApplication()

            //Second, delete the application
            val deleteResponse = restTemplate
                    .withBasicAuth(userApplicant.email, userApplicant.password)
                    .exchange(BASE_APPLICATION_URL + "/" + createResponse.body?.id,
                            HttpMethod.DELETE,
                            null,
                            Unit::class.java)

            assertThat(deleteResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        }
    }

    @Nested
    inner class accept {
        @Test
        fun `accept application of user should return 200 and increase accepted workers count`() {
            //First, create the application
            val createResponse = createApplication()

            //Second, creator of task must approve the application
            val approveResponse = restTemplate
                    .withBasicAuth(user.email, user.password)
                    .getForEntity(APPROVE_APPLICATION_URL + createResponse.body?.id, Unit::class.java)

            assertThat(approveResponse.statusCode).isEqualTo(HttpStatus.OK)

            //Check that accepted workers count is increased by 1
            val getResponse = restTemplate
                    .withBasicAuth(user.email, user.password)
                    .getForEntity(TASK_URL + "/" + taskResponse.body?.id, TaskResponse::class.java)

            assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(getResponse.body?.acceptedWorkersCount).isEqualTo(1)

            //Remove the application
            deleteApplication(createResponse.body?.id)
        }
    }

    @Nested
    inner class read {
        @Test
        fun `get list of applications for task`() {
            //Create application
            val applicationResponse = createApplication()

            val getResponse = restTemplate
                    .withBasicAuth(user.email, user.password)
                    .getForEntity("/tasks/" + taskId + "/applications?page=0&size=2", ApplicationPaginatedResponse::class.java)

            assertThat(getResponse.body?.applications).isEqualTo(listOf(applicationResponse.body))

            deleteApplication(applicationResponse.body?.id)
        }

        @Test
        fun `get list of applications from user`() {
            //Create application
            val applicationResponse = createApplication()

            val getResponse = restTemplate
                    .withBasicAuth(userApplicant.email, userApplicant.password)
                    .getForEntity("/users/me/applications?page=0&size=10", ApplicationPaginatedResponse::class.java)

            assertThat(getResponse.body?.applications).isEqualTo(listOf(applicationResponse.body))

            deleteApplication(applicationResponse.body?.id)
        }
    }


    fun createApplication(): ResponseEntity<ApplicationResponse> {
        val applicationBody = ApplicationBody(taskId!!)

        return restTemplate
                .withBasicAuth(userApplicant.email, userApplicant.password)
                .postForEntity(CREATE_APPLICATION_URL, applicationBody, ApplicationResponse::class.java)
    }

    fun deleteUser(email: String, password: String) {
        restTemplate
                .withBasicAuth(email, password)
                .delete(USER_DELETE_URL)
    }

    fun deleteApplication(applicationId: Long?) {
        restTemplate
                .withBasicAuth(userApplicant.email, userApplicant.password)
                .delete(BASE_APPLICATION_URL + "/" + applicationId)
    }
}