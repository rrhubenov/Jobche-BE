package bg.elsys.jobche.service

import bg.elsys.jobche.converter.Converters
import bg.elsys.jobche.entity.body.picture.ProfilePictureBody
import bg.elsys.jobche.entity.model.picture.ProfilePicture
import bg.elsys.jobche.entity.response.picture.PictureResponse
import bg.elsys.jobche.exception.UserNotFoundException
import bg.elsys.jobche.repository.ProfilePictureRepository
import bg.elsys.jobche.repository.TaskPictureRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class PictureService(private val profilePictureRepository: ProfilePictureRepository,
                     private val taskPictureRepository: TaskPictureRepository,
                     private val userService: UserService,
                     private val storageService: AmazonStorageService,
                     private val converters: Converters = Converters()) {
    fun addProfilePicture(profilePictureBody: ProfilePictureBody, file: MultipartFile): PictureResponse {
        if(userService.existsById(profilePictureBody.userId)) {
            val picture = ProfilePicture(profilePictureBody.userId)
            storageService.save(file.inputStream, picture.pictureId)
            return with(converters) {
                profilePictureRepository.save(picture).response
            }
        } else throw UserNotFoundException()
    }

}
