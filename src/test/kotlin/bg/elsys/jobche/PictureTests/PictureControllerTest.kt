package bg.elsys.jobche.PictureTests

import bg.elsys.jobche.BaseUnitTest
import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.controller.PictureController
import bg.elsys.jobche.service.PictureService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile

class PictureControllerTest : BaseUnitTest() {

    companion object {
        val pictureResponse = DefaultValues.pictureResponse()
        val profilePictureBody = DefaultValues.profilePictureBody()
        val picture = MockMultipartFile("profilePicture", "testData".toByteArray())
    }

    private val service: PictureService = mockk()
    private val controller = PictureController(service)

    @Nested
    inner class create {
        @Test
        fun `add profile picture should return 201`() {
            every { service.addProfilePicture(profilePictureBody, picture) } returns pictureResponse

            val result = controller.addProfilePicture(profilePictureBody, picture)

            assertThat(result.body).isEqualTo(pictureResponse)
            assertThat(result.statusCode).isEqualTo(HttpStatus.CREATED)
        }
    }
}