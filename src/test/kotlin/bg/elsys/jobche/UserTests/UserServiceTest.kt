package bg.elsys.jobche.UserTests

import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.entity.body.user.UserLoginBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.model.User
import bg.elsys.jobche.entity.response.user.UserResponse
import bg.elsys.jobche.repositories.UserRepository
import bg.elsys.jobche.service.UserService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
class UserServiceTest {

    companion object {
        const val FIRST_NAME = "Radoslav"
        const val LAST_NAME = "Hubenov"
        const val EMAIL = "rrhubenov@gmail.com"
        const val PASSWORD = "password"
        val DATE_OF_BIRTH = DateOfBirth(1,1,2000)
        private val user = User(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, DATE_OF_BIRTH.toString())
        private val userRegister = UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, DATE_OF_BIRTH)
        private val userLogin = UserLoginBody(EMAIL, PASSWORD)
    }

    private val repository: UserRepository = mockk()
    private val authenticationDetails: AuthenticationDetails = mockk()
    private val passwordEncoder = BCryptPasswordEncoder()

    private val userService: UserService

    init {
        userService = UserService(repository, passwordEncoder, authenticationDetails)
    }

    @Test
    fun `login should return valid user response`() {
        every { repository.existsByEmail(EMAIL) } returns true
        every { repository.findByEmail(EMAIL) } returns user

        val result = userService.login(userLogin)
        val expectedResult = UserResponse(user.id, "Radoslav", "Hubenov", DATE_OF_BIRTH)
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `create should return valid user response`() {
        every { repository.existsByEmail(userRegister.email) } returns false
        every { repository.save(any<User>()) } returns user

        val userResponse = userService.create(userRegister)

        assertThat(userResponse).isEqualTo(UserResponse(user.id, userRegister.firstName,
                userRegister.lastName, DATE_OF_BIRTH))
    }

    @Test
    fun `delete user`() {
        every { authenticationDetails.getEmail() } returns anyString()
        every { repository.deleteByEmail(any()) } returns Unit

        userService.delete()

        verifyAll {
            authenticationDetails.getEmail()
            repository.deleteByEmail(anyString())  }
    }

    @Test
    fun `update user`() {
        every { repository.getOneByEmail(anyString()) } returns user
        every { repository.save(user) } returns user

        userService.update( UserRegisterBody(user.firstName, user.lastName, user.email, user.password, DATE_OF_BIRTH))

        verify {
            repository.getOneByEmail(anyString())
            repository.save(user)
        }
    }

    @Test
    fun `read user`() {
        every { repository.existsById(anyLong()) } returns true
        every { repository.findById(anyLong()) } returns Optional.of(user)

        userService.read(anyLong())

        verify {
            repository.existsById(anyLong())
            repository.findById(anyLong())
        }
    }

    @Test
    fun `test dateOfBirth string to DateOfBirth`() {
        val dateOfBirthString = "1-1-2000"

        assertThat(userService.toDateOfBirth(dateOfBirthString)).isEqualTo(DATE_OF_BIRTH)
    }
}
