package bg.elsys.jobche.ApplicationTasks

import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.controller.ApplicationController
import bg.elsys.jobche.entity.body.application.ApplicationBody
import bg.elsys.jobche.entity.model.Application
import bg.elsys.jobche.entity.response.application.ApplicationResponse
import bg.elsys.jobche.service.ApplicationService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.springframework.http.HttpStatus

@ExtendWith(MockKExtension::class)
class ApplicationControllerTest {

    companion object {
        const val ID = 1L
        val user = DefaultValues.user
        val task = DefaultValues.task
    }

    val applicationBody: ApplicationBody
    val applicationResponse: ApplicationResponse
    private val controller: ApplicationController
    private val service: ApplicationService = mockk()
    private val application = Application(user, task)

    init {
        controller = ApplicationController(service)
        applicationResponse = ApplicationResponse(ID, user.id, task.id, false)
        applicationBody = ApplicationBody(task.id)
    }

    @Test
    fun create() {
        every { service.create(applicationBody) } returns application

        val result = controller.create(applicationBody)

        assertThat(result.body).isEqualTo(applicationResponse)
    }

    @Test
    fun `delete should return 204`() {
        every { service.delete(ID) } returns Unit

        val result = controller.delete(ID)

        assertThat(result.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `approve application should return 200`() {
        every { service.approveApplication(anyLong()) } returns Unit

        val result = controller.approveApplication(anyLong())

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
}