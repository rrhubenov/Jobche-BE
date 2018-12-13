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
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    companion object {
        const val FIRST_NAME = "Radoslav"
        const val LAST_NAME = "Hubenov"
        const val EMAIL = "rrhubenov@gmail.com"
        const val PASSWORD = "password"
        private val userDTO = User(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)
        private val userRegister = UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)
        private val userLogin = UserLoginBody(EMAIL, PASSWORD)
    }

    private val repository: UserRepository = mockk()
    private val passwordEncoder = BCryptPasswordEncoder()

    private val userService: UserService

    init {
        userService = UserService(repository, passwordEncoder)
    }

    @Test
    fun loginTest() {
        every { repository.findByEmail(EMAIL) } returns userDTO

        val result = userService.login(userLogin)
        val expectedResult = UserResponse(0, "Radoslav", "Hubenov")
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun registerTest() {
        every { repository.save(any<User>()) } returns userDTO
        val userResponse = userService.register(userRegister)
        assertThat(userResponse).isEqualTo(UserResponse(anyLong(), userRegister.firstName, userRegister.lastName))
    }

}
