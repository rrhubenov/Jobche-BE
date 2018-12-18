package bg.elsys.jobche.UserTests

import bg.elsys.jobche.controller.UserController
import bg.elsys.jobche.entity.body.UserLoginBody
import bg.elsys.jobche.entity.body.UserRegisterBody
import bg.elsys.jobche.entity.response.UserResponse
import bg.elsys.jobche.exceptions.UserNotFoundException
import bg.elsys.jobche.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
    fun testLogin() {
        val userLogin = UserLoginBody(EMAIL, PASSWORD)

        every { userService.login(userLogin)} returns userResponse

        val result = controller.login(userLogin)

        assertThat(result.body).isEqualTo(userResponse)
    }

    @Test
    fun testRegister() {
        val userRegisterBody = UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)

        every { userService.register(userRegisterBody) } returns userResponse

        val result = controller.register(userRegisterBody)

        assertThat(result.body).isEqualTo(userResponse)
    }
}
