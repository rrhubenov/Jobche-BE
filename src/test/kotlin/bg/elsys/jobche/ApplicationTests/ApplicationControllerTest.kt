package bg.elsys.jobche.ApplicationTests

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
        private val applicationResponse = DefaultValues.applicationResponse
        private val applicationBody = DefaultValues.applicationBody
        private val application = DefaultValues.application

    }
    private val controller: ApplicationController
    private val service: ApplicationService = mockk()

    init {
        controller = ApplicationController(service)
        applicationResponse = DefaultValues.applicationResponse
        applicationBody = ApplicationBody(task.id)
    }

    @Test
    fun `create should return application response`() {
        every { service.create(applicationBody) } returns application

        val result = controller.create(applicationBody)

        assertThat(result.body).isEqualTo(applicationResponse)
    }

    @Test
    fun `delete should return 204`() {
        every { service.delete(application.id) } returns Unit

        val result = controller.delete(application.id)

        assertThat(result.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `approve application should return 200`() {
        every { service.approveApplication(anyLong()) } returns Unit

        val result = controller.approveApplication(anyLong())

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
}