package bg.elsys.jobche.service

import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.entity.body.application.ApplicationBody
import bg.elsys.jobche.entity.model.task.Application
import bg.elsys.jobche.entity.model.task.Task
import bg.elsys.jobche.exception.ResourceForbiddenException
import bg.elsys.jobche.exception.ResourceNotFoundException
import bg.elsys.jobche.exception.TaskNotFoundException
import bg.elsys.jobche.exception.UserNotFoundException
import bg.elsys.jobche.exception.NoContentException
import bg.elsys.jobche.repository.ApplicationRepository
import bg.elsys.jobche.repository.TaskRepository
import bg.elsys.jobche.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ApplicationService(val appRepository: ApplicationRepository,
                         val userRepository: UserRepository,
                         val taskRepository: TaskRepository,
                         val authenticationDetails: AuthenticationDetails) {
    fun create(application: ApplicationBody): Application {
        if (taskRepository.existsById(application.taskId)) {
            val user = userRepository.findByEmail(authenticationDetails.getEmail())
            val task = taskRepository.findById(application.taskId).get()
            if (user != null) {
                return appRepository.save(Application(user, task))
            } else throw UserNotFoundException()
        } else throw TaskNotFoundException()
    }

    fun delete(id: Long) {
        if (appRepository.existsById(id)) {
            val user = userRepository.findByEmail(authenticationDetails.getEmail())
            val app = appRepository.findById(id).get()
            if (app.user.id == user?.id) {
                appRepository.deleteById(id)
            } else throw ResourceForbiddenException()
        } else throw ResourceNotFoundException()
    }

    fun approveApplication(id: Long) {
        if (appRepository.existsById(id)) {
            val user = userRepository.findByEmail(authenticationDetails.getEmail())
            val application = appRepository.getOne(id)
            if (application.task?.creator?.id == user?.id) {
                application.accepted = true
                application.task!!.acceptedWorkersCount++
                appRepository.save(application)
            } else throw ResourceForbiddenException()
        } else throw ResourceNotFoundException()
    }

    fun getApplicationsForTask(taskId: Long, page: Int, size: Int): List<Application> {
        val user = userRepository.findByEmail(authenticationDetails.getEmail())
        val task: Task

        if (taskRepository.existsById(taskId)) {
            task = taskRepository.findById(taskId).get()
        } else throw ResourceNotFoundException()

        if (task.creator.id == user?.id) {
            val result = appRepository.findAll(createPageRequest(page, size)).content

            if (result.isEmpty()) {
                throw NoContentException()
            } else return result

        } else throw ResourceForbiddenException()
    }

    fun getApplicationsForUser(page: Int, size: Int): List<Application> {
        val user = userRepository.findByEmail(authenticationDetails.getEmail())

        val result = appRepository.findAllByUser(createPageRequest(page, size), user).content

        if (result.isEmpty()) {
            throw NoContentException()
        } else return result
    }

    private fun createPageRequest(page: Int, size: Int): Pageable {
        return PageRequest.of(page, size)
    }

}
