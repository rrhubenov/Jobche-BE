package bg.elsys.jobche.UserTests

import bg.elsys.jobche.BaseUnitTest
import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.converter.Converters
import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.model.user.User
import bg.elsys.jobche.entity.response.user.UserResponse
import bg.elsys.jobche.exception.EmailExistsException
import bg.elsys.jobche.exception.PhoneNumberExistsException
import bg.elsys.jobche.repository.UserRepository
import bg.elsys.jobche.service.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

class UserServiceTest: BaseUnitTest() {

    companion object {
        const val FIRST_NAME = "Radoslav"
        const val LAST_NAME = "Hubenov"
        const val EMAIL = "rrhubenov@gmail.com"
        const val PASSWORD = "password"
        val DATE_OF_BIRTH = DateOfBirth(1, 1, 2000)
        const val PHONE_NUM = "0878555373"
        private val user = User(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, DATE_OF_BIRTH.toString(), PHONE_NUM)
        private val userRegister = DefaultValues.creatorUserRegisterBody
        private val userLogin = DefaultValues.creatorUserLoginBody
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
        val expectedResult = UserResponse(user.id, user.firstName, "Hubenov", DATE_OF_BIRTH, PHONE_NUM)
        assertThat(result).isEqualTo(expectedResult)
    }

    @Nested
    inner class create {
        @Test
        fun `create should return valid user response`() {
            every { repository.existsByEmail(userRegister.email) } returns false
            every { repository.existsByPhoneNum(userRegister.phoneNum) } returns false
            every { repository.save(any<User>()) } returns user

            val userResponse = userService.create(userRegister)

            assertThat(userResponse).isEqualTo(UserResponse(user.id, userRegister.firstName,
                    userRegister.lastName, DATE_OF_BIRTH, PHONE_NUM))
        }

        @Test
        fun `create with already existing phone should throw exception`() {
            every { repository.existsByEmail(userRegister.email) } returns false
            every { repository.existsByPhoneNum(userRegister.phoneNum) } returns true
            every { repository.save(any<User>()) } returns user

            assertThatExceptionOfType(PhoneNumberExistsException::class.java).isThrownBy {
                userService.create(userRegister)
            }
        }

        @Test
        fun `create with already existing email should throw exception`() {
            every { repository.existsByEmail(userRegister.email) } returns true
            every { repository.existsByPhoneNum(userRegister.phoneNum) } returns true
            every { repository.save(any<User>()) } returns user

            assertThatExceptionOfType(EmailExistsException::class.java).isThrownBy {
                userService.create(userRegister)
            }
        }
    }

    @Test
    fun `delete user`() {
        every { authenticationDetails.getEmail() } returns anyString()
        every { repository.deleteByEmail(any()) } returns Unit

        userService.delete()

        verifyAll {
            authenticationDetails.getEmail()
            repository.deleteByEmail(anyString())
        }
    }

    @Test
    fun `update user`() {
        every { repository.getOneByEmail(anyString()) } returns user
        every { repository.save(user) } returns user

        userService.update(UserRegisterBody(user.firstName, user.lastName, user.email, user.password, DATE_OF_BIRTH, user.phoneNum))

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

        assertThat(Converters().toDateOfBirth(dateOfBirthString)).isEqualTo(DATE_OF_BIRTH)
    }
}