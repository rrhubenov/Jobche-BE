package bg.elsys.jobche.UserTests

import bg.elsys.jobche.controller.UserController
import bg.elsys.jobche.entity.body.user.UserLoginBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.response.UserResponse
import bg.elsys.jobche.service.UserService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
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
        val userResponse = UserResponse(ID, FIRST_NAME, LAST_NAME)
    }

    private val userService: UserService = mockk()

    private val controller: UserController

    init {
        controller = UserController(userService)
    }

    @Test
    fun `test response for login`() {
        val userLogin = UserLoginBody(EMAIL, PASSWORD)

        every { userService.login(userLogin) } returns userResponse

        val result = controller.login(userLogin)

        assertThat(result.body).isEqualTo(userResponse)
    }

    @Test
    fun `test response for register`() {
        val userRegisterBody = UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)

        every { userService.register(userRegisterBody) } returns userResponse

        val result = controller.register(userRegisterBody)

        assertThat(result.body).isEqualTo(userResponse)
    }

    @Test
    fun `remove should return userResponse`() {
        every { userService.delete(anyLong()) } returns Unit
        val result = controller.delete(anyLong())

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
}
