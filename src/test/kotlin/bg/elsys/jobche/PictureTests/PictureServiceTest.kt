package bg.elsys.jobche.PictureTests

import bg.elsys.jobche.BaseUnitTest
import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.converter.Converters
import bg.elsys.jobche.repository.ProfilePictureRepository
import bg.elsys.jobche.repository.TaskPictureRepository
import bg.elsys.jobche.repository.UserRepository
import bg.elsys.jobche.service.AmazonStorageService
import bg.elsys.jobche.service.PictureService
import bg.elsys.jobche.service.TaskService
import bg.elsys.jobche.service.UserService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile

class PictureServiceTest : BaseUnitTest() {
    companion object {
        val profilePicture = DefaultValues.profilePicture()
        val picture = MockMultipartFile("profilePicture", "testData".toByteArray())
        val pictureResponse = DefaultValues.pictureResponse()
        val user = DefaultValues.workerUser()
    }

    val profilePictureRepository: ProfilePictureRepository = mockk()
    val taskPictureRepository: TaskPictureRepository = mockk()
    val userRepository: UserRepository = mockk()
    val taskService: TaskService = mockk()
    val storageService: AmazonStorageService = mockk()
    val authenticationDetails: AuthenticationDetails = mockk()
    val converters = Converters(storageService)
    val pictureService = PictureService(profilePictureRepository, taskPictureRepository, userRepository, taskService, storageService, authenticationDetails, converters)

    @Nested
    inner class create {
        @Test
        fun `adding a profile picture should return the right response`() {
            every { authenticationDetails.getUser() } returns user
            every { storageService.save(file = any(), id = any()) } returns profilePicture.pictureId
            every { profilePictureRepository.save(profilePicture) } returns profilePicture

            val result = pictureService.addProfilePicture(picture)

            assertThat(result).isEqualTo(pictureResponse)
        }
    }
}