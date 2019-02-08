package bg.elsys.jobche.UserTests

import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.controller.UserController
import bg.elsys.jobche.converter.Converters
import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.entity.body.user.UserLoginBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.response.user.UserResponse
import bg.elsys.jobche.service.ApplicationService
import bg.elsys.jobche.service.UserService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.springframework.http.HttpStatus

@ExtendWith(MockKExtension::class)
class UserControllerTest() {

    companion object {
        val userResponse = DefaultValues.userResponse
        val userRegisterBody = DefaultValues.userRegisterBody
        val userLoginBody = DefaultValues.userLoginBody
    }

    private val userService: UserService = mockk()
    private val applicationService: ApplicationService = mockk()

    private val controller: UserController

    init {
        controller = UserController(userService, applicationService)
    }

    @Test
    fun `login should return valid user response`() {
        every { userService.login(userLoginBody) } returns userResponse

        val result = controller.login(userLoginBody)

        assertThat(result.body).isEqualTo(userResponse)
    }

    @Test
    fun `create should return valid user response`() {
        every { userService.create(userRegisterBody) } returns userResponse

        val result = controller.create(userRegisterBody)

        assertThat(result.body).isEqualTo(userResponse)
    }

    @Test
    fun `remove should return 204`() {
        every { userService.delete() } returns Unit
        val result = controller.delete()

        assertThat(result.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `update should return 200`() {
        every { userService.update(userRegisterBody) } returns Unit

        val result = controller.update(userRegisterBody)

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
        every { applicationService.getApplicationsForUser(1, 1) } returns listOf(DefaultValues.application)

        val result = controller.getApplications(1, 1)

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body?.applications).isEqualTo(listOf(DefaultValues.applicationResponse))
    }
}
