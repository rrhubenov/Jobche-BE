package bg.elsys.jobche.UserTests

import bg.elsys.jobche.BaseUnitTest
import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.controller.UserController
import bg.elsys.jobche.converter.Converters
import bg.elsys.jobche.service.AmazonStorageService
import bg.elsys.jobche.service.ApplicationService
import bg.elsys.jobche.service.UserService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.springframework.http.HttpStatus

class UserControllerTest : BaseUnitTest() {

    companion object {
        val userResponse = DefaultValues.creatorUserResponse()
        val userBody = DefaultValues.creatorUserBody()
    }

    private val userService: UserService = mockk()
    private val applicationService: ApplicationService = mockk()
    private val storageService: AmazonStorageService = mockk()
    private val converters = Converters(storageService)


    private val controller = UserController(userService, applicationService, converters)

    @Nested
    inner class create() {
        @Test
        fun `create should return valid user response`() {
            every { userService.create(userBody) } returns userResponse

            val result = controller.create(userBody)

            assertThat(result.body).isEqualTo(userResponse)
        }
    }

    @Test
    fun `remove should return 204`() {
        every { userService.delete() } returns Unit
        val result = controller.delete()

        assertThat(result.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `update should return 200`() {
        every { userService.update(userBody) } returns Unit

        val result = controller.update(userBody)

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `read should return 200 and valid user response`() {
        every { userService.read(anyLong()) } returns userResponse

        val result = controller.read(anyLong())

        assertThat(result.body).isEqualTo(userResponse)
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `get applications created from user should return 200 and appplications list`() {
        every { applicationService.getApplicationsForUser(1, 1) } returns listOf(DefaultValues.application())

        val result = controller.getApplications(1, 1)

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body?.applications).isEqualTo(listOf(DefaultValues.applicationResponse()))
    }
}
