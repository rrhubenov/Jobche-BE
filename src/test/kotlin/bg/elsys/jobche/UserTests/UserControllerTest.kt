package bg.elsys.jobche.UserTests

import bg.elsys.jobche.controller.UserController
import bg.elsys.jobche.entity.body.UserLoginBody
import bg.elsys.jobche.entity.response.UserResponse
import bg.elsys.jobche.exceptions.UserNotFoundException
import bg.elsys.jobche.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
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

@ExtendWith(SpringExtension::class)
@WebMvcTest(UserController::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest() {

    companion object {
        const val BASE_URL = "/user"
        const val LOGIN_URL = BASE_URL + "/login"
        const val REGISTER_URL = BASE_URL + "/register"
        const val EMAIL = "rrhubenov@gmail.com"
        const val PASSWORD = "password"
        val userLogin = UserLoginBody(EMAIL, PASSWORD)
        lateinit var userLoginJson : String
    }

    @MockBean
    lateinit var userService: UserService

    @Autowired
    lateinit var mockMvc: MockMvc

    init {
        val mapper = ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false)
        val writer = mapper.writer().withDefaultPrettyPrinter()
        userLoginJson = writer.writeValueAsString(userLogin)
    }


    @Test
    fun login() {
        given(userService.login(userLogin)).willReturn(UserResponse(1, "Radoslav", "Hubenov"))

        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                .content(userLoginJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("firstName").value("Radoslav"))
                .andExpect(jsonPath("lastName").value("Hubenov"))
    }

    @Test
    fun loginUserNotFound() {
        given(userService.login(userLogin)).willThrow(UserNotFoundException())

        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                .content(userLoginJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound)
    }
}
