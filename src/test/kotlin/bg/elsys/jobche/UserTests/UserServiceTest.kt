package bg.elsys.jobche.UserTests

import bg.elsys.jobche.entity.body.UserLoginBody
import bg.elsys.jobche.entity.body.UserRegisterBody
import bg.elsys.jobche.entity.model.User
import bg.elsys.jobche.entity.response.UserResponse
import bg.elsys.jobche.repositories.UserRepository
import bg.elsys.jobche.service.UserService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import java.util.*

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    companion object {
        const val FIRST_NAME = "Radoslav"
        const val LAST_NAME = "Hubenov"
        const val EMAIL = "rrhubenov@gmail.com"
        const val PASSWORD = "password"
        private val userModel = User(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)
        private val userRegister = UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)
        private val userLogin = UserLoginBody(EMAIL, PASSWORD)
    }

    private val userRepository: UserRepository = mockk()

    private val userService: UserService

    init {
        userService = UserService(userRepository)
    }

    @Test
    fun loginTest() {
        every { userRepository.findByEmail(EMAIL) } returns userModel

        val result = userService.login(userLogin)
        val expectedResult = UserResponse(0, "Radoslav", "Hubenov")
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun registerTest() {
        every { userRepository.save(userModel) } returns userModel
        assertThat(userService.register(userRegister)).isEqualTo(UserResponse(anyLong(), userRegister.firstName, userRegister.lastName))
    }

}
