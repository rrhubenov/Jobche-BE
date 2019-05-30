package bg.elsys.jobche.Utils

import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.UserTests.UserServiceTest
import bg.elsys.jobche.converter.Converters
import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.service.AmazonStorageService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConvertersTest {
    companion object {
        val DATE_OF_BIRTH = DateOfBirth(1, 1, 2000)

        val taskResponse = DefaultValues.taskResponse()
        val userResponse = DefaultValues.creatorUserResponse()
        val applicationResponse = DefaultValues.applicationResponse()
        val pictureResponse = DefaultValues.pictureResponse()
    }

    val storageService: AmazonStorageService = mockk()
    val converters = Converters(storageService)

    @Test
    fun `test task response conversion`() {
        val task = DefaultValues.task()

        with(converters) {
            assertThat(task.response).isEqualTo(taskResponse)
        }
    }

    @Test
    fun `test user response conversion`() {
        val user = DefaultValues.creatorUser()

        with(converters) {
            assertThat(user.response).isEqualTo(userResponse)
        }
    }

    @Test
    fun `test application response conversion`() {
        val application = DefaultValues.application()

        with(converters) {
            assertThat(application.response).isEqualTo(applicationResponse)
        }
    }

    @Test
    fun `test picture response conversion`() {
        val profilePicture = DefaultValues.profilePicture()

        with(converters) {
            assertThat(profilePicture.response).isEqualTo(pictureResponse)
        }
    }

    @Test
    fun `test dateOfBirth string to DateOfBirth`() {
        val dateOfBirthString = "1-1-2000"

        assertThat(converters.toDateOfBirth(dateOfBirthString)).isEqualTo(DATE_OF_BIRTH)
    }
}