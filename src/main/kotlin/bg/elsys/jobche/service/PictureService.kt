package bg.elsys.jobche.service

import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.converter.Converters
import bg.elsys.jobche.entity.model.picture.ProfilePicture
import bg.elsys.jobche.entity.model.picture.TaskPicture
import bg.elsys.jobche.entity.response.TaskPicturesResponse
import bg.elsys.jobche.entity.response.picture.PictureResponse
import bg.elsys.jobche.exception.NoContentException
import bg.elsys.jobche.exception.ResourceForbiddenException
import bg.elsys.jobche.exception.UserNotFoundException
import bg.elsys.jobche.repository.ProfilePictureRepository
import bg.elsys.jobche.repository.TaskPictureRepository
import bg.elsys.jobche.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@Service
class PictureService(private val profilePictureRepository: ProfilePictureRepository,
                     private val taskPictureRepository: TaskPictureRepository,
                     private val userRepository: UserRepository,
                     private val taskService: TaskService,
                     private val storageService: AmazonStorageService,
                     private val authenticationDetails: AuthenticationDetails,
                     private val converters: Converters) {
    fun addProfilePicture(file: MultipartFile): PictureResponse {
        val user = authenticationDetails.getUser()
        val picture = ProfilePicture(user)
        if (user.picture != null) {
            val oldPicture = user.picture
            if (oldPicture != null) {
                storageService.delete(oldPicture.pictureId)
            }
        }
        storageService.save(file.inputStream, picture.pictureId)
        return with(converters) {
            profilePictureRepository.save(picture).response
        }
    }

    fun getProfilePicture(id: Long?): InputStream {

        val userId: Long
        if (id == null) {
            userId = authenticationDetails.getId()
        } else userId = id

        if (userRepository.existsById(userId)) {
            val user = userRepository.findById(userId).get()
            if (profilePictureRepository.existsByUser(user)) {
                return storageService.read(profilePictureRepository.findByUser(user).pictureId)
            } else throw NoContentException("Exception: User has no profile picture")
        } else throw UserNotFoundException()
    }

    fun addTaskPicture(file: MultipartFile, id: Long): PictureResponse {
        val user = authenticationDetails.getUser()
        val task = taskService.getById(id)

        if (task.creator.id == user.id) {
            val picture = TaskPicture(task)
            with(converters) {
                storageService.save(file.inputStream, picture.pictureId)
                return taskPictureRepository.save(picture).response
            }
        } else throw ResourceForbiddenException("Exception: You do not have permission to add pictures for this task")
    }

    fun getPicturesForTask(id: Long): TaskPicturesResponse {
        val task = taskService.getById(id)

        val hyperlinks = mutableListOf<String?>()

        val pictures = task.pictures
        if (pictures != null) {
            for (picture in pictures) {
                hyperlinks.add(storageService.url(picture.pictureId))
            }
            return TaskPicturesResponse(hyperlinks)
        } else throw NoContentException("Exception: Task has no pictures")
    }

}
