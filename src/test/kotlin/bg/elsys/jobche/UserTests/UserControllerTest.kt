package bg.elsys.jobche.UserTests

import bg.elsys.jobche.controller.UserController
import bg.elsys.jobche.entity.response.UserResponse
import bg.elsys.jobche.exceptions.UserNotFoundException
import bg.elsys.jobche.service.UserService
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(UserController::class)
class UserControllerTest() {

    companion object {
        const val testUrl = "/users/1"
    }

    @MockBean
    lateinit var userService: UserService

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun getUser() {
        given(userService.getUser(anyLong())).willReturn(UserResponse(1, "Radoslav", "Hubenov"))

        mockMvc.perform(MockMvcRequestBuilders.get(testUrl))
                .andExpect(status().isOk)
                .andExpect(jsonPath("firstName").value("Radoslav"))
                .andExpect(jsonPath("lastName").value("Hubenov"))
    }

    @Test
    fun getUserNotFound() {
        given(userService.getUser(anyLong())).willThrow(UserNotFoundException())

        mockMvc.perform(MockMvcRequestBuilders.get(testUrl))
                .andExpect(status().isNotFound)
    }
}
