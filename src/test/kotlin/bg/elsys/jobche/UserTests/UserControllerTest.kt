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
        const val ID = 1L
        const val FIRST_NAME = "Radosalv"
        const val LAST_NAME = "Hubenov"
        const val EMAIL = "rrhubenov@gmail.com"
        const val PASSWORD = "password"
        val DATE_OF_BIRTH = DateOfBirth(1, 1, 2000)
        val userResponse = UserResponse(ID, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH)
    }

    private val userService: UserService = mockk()
    private val applicationService: ApplicationService = mockk()

    private val controller: UserController

    init {
        controller = UserController(userService, applicationService)
    }

    @Test
    fun `login should return valid user response`() {
        val userLogin = UserLoginBody(EMAIL, PASSWORD)

        every { userService.login(userLogin) } returns userResponse

        val result = controller.login(userLogin)

        assertThat(result.body).isEqualTo(userResponse)
    }

    @Test
    fun `create should return valid user response`() {
        val userRegisterBody = UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, DATE_OF_BIRTH)

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
        val updatedUser = UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, DATE_OF_BIRTH)

        every { userService.update(updatedUser) } returns Unit

        val result = controller.update(updatedUser)

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
