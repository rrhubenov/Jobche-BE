package bg.elsys.jobche.UserTests

import bg.elsys.jobche.entity.User
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
import org.mockito.Mockito.`when`
import java.util.*

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    private val userRepository: UserRepository = mockk()

    private val userService: UserService

    private val user = User("Radoslav", "Hubenov")

    init {
        userService = UserService(userRepository)
    }

    @Test
    fun getUserTest() {
        val optional = Optional.of(user)
        every { userRepository.findById(1) } returns optional

        val result = userService.getUser(1)
        val expectedResult = UserResponse(0, "Radoslav", "Hubenov")
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun addUserTest() {
        every { userRepository.save(user) } returns user
        assertThat(userService.addUser(user)).isEqualTo(user.id)
    }

}
