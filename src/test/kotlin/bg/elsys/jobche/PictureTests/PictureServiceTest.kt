package bg.elsys.jobche.PictureTests

import bg.elsys.jobche.BaseUnitTest
import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.repository.ProfilePictureRepository
import bg.elsys.jobche.repository.TaskPictureRepository
import bg.elsys.jobche.service.AmazonStorageService
import bg.elsys.jobche.service.PictureService
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
        val profilePictureBody = DefaultValues.profilePictureBody()
        val picture = MockMultipartFile("profilePicture", "testData".toByteArray())
        val pictureResponse = DefaultValues.pictureResponse()
    }

    val profilePictureRepository: ProfilePictureRepository = mockk()
    val taskPictureRepository: TaskPictureRepository = mockk()
    val userService: UserService = mockk()
    val storageService: AmazonStorageService = mockk()
    val pictureService = PictureService(profilePictureRepository, taskPictureRepository, userService, storageService)

    @Nested
    inner class create {
        @Test
        fun `adding a profile picture should return the right response`() {
            every { userService.existsById(profilePictureBody.userId) } returns true
            every { storageService.save(file = any(), id = any()) } returns profilePicture.pictureId
            every { profilePictureRepository.save(profilePicture) } returns profilePicture

            val result = pictureService.addProfilePicture(profilePictureBody, picture)

            assertThat(result).isEqualTo(pictureResponse)
        }
    }
}