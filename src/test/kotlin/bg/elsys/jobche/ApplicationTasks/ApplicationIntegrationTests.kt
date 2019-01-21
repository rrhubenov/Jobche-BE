package bg.elsys.jobche.ApplicationTasks

import bg.elsys.jobche.entity.body.application.ApplicationBody
import bg.elsys.jobche.entity.body.task.Address
import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.response.application.ApplicationResponse
import bg.elsys.jobche.entity.response.task.TaskResponse
import bg.elsys.jobche.entity.response.user.UserResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class ApplicationIntegrationTests {
    companion object {
        //User constants
        const val FIRST_NAME = "Radoslav"
        const val LAST_NAME = "Hubenov"
        val DATE_OF_BIRTH = DateOfBirth(1, 1, 2000)
        const val EMAIL = "random@random.com"
        const val PASSWORD = "random"
        const val APPLICANT_EMAIL = "applicant@app.com"
        const val APPLICANT_PASSWORD = "randompass"
        const val REGISTER_URL = "/users"
        const val USER_DELETE_URL = "/users"
        //Task constants
        const val CREATE_URL = "/tasks"
        const val TASK_TITLE = "Test Title"
        const val TASK_PAYMENT = 1
        const val TASK_NUMBER_OF_WORKERS = 1
        const val TASK_DESCRIPTION = "Test Description"
        val TASK_TIME_OF_WORK = LocalDateTime.now()
        val TASK_LOCATION = Address("Bulgaria", "Sofia", "Krasno Selo")
        //Application constants
        const val BASE_APPLICATION_URL = "/application"
        const val CREATE_APPLICATION_URL = BASE_APPLICATION_URL
        const val APPROVE_APPLICATION_URL = BASE_APPLICATION_URL + "/approve/"

    }

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    lateinit var userCreatorResponse: ResponseEntity<UserResponse>
    lateinit var userApplicantResponse: ResponseEntity<UserResponse>
    lateinit var taskResponse: ResponseEntity<TaskResponse>
    var taskId: Long? = 0
    var userCreatorId: Long? = 0
    var userApplicantId: Long? = 0

    @BeforeEach
    fun setup() {
        val userCreatorBody = UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, DATE_OF_BIRTH)
        userCreatorResponse = restTemplate.postForEntity(REGISTER_URL, userCreatorBody, UserResponse::class.java)
        userCreatorId = userCreatorResponse.body?.id

        val userApplicantBody = UserRegisterBody("Applicant", "Applicant", APPLICANT_EMAIL, APPLICANT_PASSWORD, DateOfBirth(2, 3, 2001))

        userApplicantResponse = restTemplate.postForEntity(REGISTER_URL, userApplicantBody, UserResponse::class.java)
        userApplicantId = userApplicantResponse.body?.id

        val taskBody = TaskBody(TASK_TITLE, TASK_PAYMENT, TASK_NUMBER_OF_WORKERS, TASK_DESCRIPTION, TASK_TIME_OF_WORK, TASK_LOCATION)

        taskResponse = restTemplate
                .withBasicAuth(EMAIL, PASSWORD)
                .postForEntity(CREATE_URL, taskBody, TaskResponse::class.java)

        taskId = taskResponse.body?.id
    }

    @AfterEach
    fun cleanUp() {
        restTemplate
                .withBasicAuth(EMAIL, PASSWORD)
                .delete("/tasks/" + taskId)

        restTemplate
                .withBasicAuth(EMAIL, PASSWORD)
                .delete(USER_DELETE_URL)

        restTemplate
                .withBasicAuth(APPLICANT_EMAIL, APPLICANT_PASSWORD)
                .delete(USER_DELETE_URL)

        System.out.print("ads")
    }

    @Test
    fun `create application should return 201 and the expected response`() {
        val applicationResponse = createApplication()

        val expectedResponse = ApplicationResponse(applicationResponse.body?.id, userApplicantId!!, taskId!!, false)

        restTemplate
                .withBasicAuth(APPLICANT_EMAIL, APPLICANT_PASSWORD)
                .delete(BASE_APPLICATION_URL + "/" + applicationResponse.body?.id)

        assertThat(applicationResponse.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(applicationResponse.body).isEqualTo(expectedResponse)

    }

    @Test
    fun `delete application should return 204`() {
        //First, create the application
        val createResponse = createApplication()

        //Second, delete the application
        val deleteResponse = restTemplate
                .withBasicAuth(APPLICANT_EMAIL, APPLICANT_PASSWORD)
                .exchange(BASE_APPLICATION_URL + "/" + createResponse.body?.id,
                        HttpMethod.DELETE,
                        null,
                        Unit::class.java)

        assertThat(deleteResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `accept application of user should return 200`() {
        //First, create the application
        val createResponse = createApplication()

        //Second, creator of task must approve the application
        val approveResponse = restTemplate
                .withBasicAuth(EMAIL, PASSWORD)
                .getForEntity(APPROVE_APPLICATION_URL + createResponse.body?.id, Unit::class.java)

        assertThat(approveResponse.statusCode).isEqualTo(HttpStatus.OK)

        //Remove the application
        restTemplate
                .withBasicAuth(APPLICANT_EMAIL, APPLICANT_PASSWORD)
                .delete(BASE_APPLICATION_URL + "/" + createResponse.body?.id)
    }

    fun createApplication(): ResponseEntity<ApplicationResponse> {
        val applicationBody = ApplicationBody(taskId!!)

        return restTemplate
                .withBasicAuth(APPLICANT_EMAIL, APPLICANT_PASSWORD)
                .postForEntity(CREATE_APPLICATION_URL, applicationBody, ApplicationResponse::class.java)
    }
}