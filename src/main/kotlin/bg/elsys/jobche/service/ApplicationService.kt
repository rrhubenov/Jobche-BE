package bg.elsys.jobche.service

import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.entity.body.application.ApplicationBody
import bg.elsys.jobche.entity.model.task.Application
import bg.elsys.jobche.entity.model.task.Task
import bg.elsys.jobche.exception.NoContentException
import bg.elsys.jobche.exception.ResourceForbiddenException
import bg.elsys.jobche.exception.ResourceNotFoundException
import bg.elsys.jobche.exception.TaskNotFoundException
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
            val user = authenticationDetails.getUser()
            val task = taskRepository.findById(application.taskId).get()
            return appRepository.save(Application(user, task))
        } else throw TaskNotFoundException()
    }

    fun delete(id: Long) {
        if (appRepository.existsById(id)) {
            val user = authenticationDetails.getUser()
            val app = appRepository.findById(id).get()
            if (app.user.id == user.id) {
                appRepository.deleteById(id)
            } else throw ResourceForbiddenException("Exception: You do not have permission to modify this application")
        } else throw ResourceNotFoundException("Exception: No application matching this id was found")
    }

    fun changeApplicationStatus(id: Long, value: Boolean) {
        if (appRepository.existsById(id)) {
            val user = authenticationDetails.getUser()
            val application = appRepository.getOne(id)
            if (application.task?.creator?.id == user.id) {
                application.accepted = value
                if (value == true) {
                    application.task.acceptedWorkersCount++
                } else application.task.acceptedWorkersCount--
                appRepository.save(application)
            } else throw ResourceForbiddenException("Exception: You do not have permission to modify this application")
        } else throw ResourceNotFoundException("Exception: No application matching this id was found")
    }

    fun getApplicationsForTask(taskId: Long, page: Int, size: Int): List<Application> {
        val user = userRepository.findByEmail(authenticationDetails.getEmail())
        val task: Task

        if (taskRepository.existsById(taskId)) {
            task = taskRepository.findById(taskId).get()
        } else throw ResourceNotFoundException()

        if (task.creator.id == user?.id) {
            val result = appRepository.findAllByTask(createPageRequest(page, size), task).content

            if (result.isEmpty()) {
                throw NoContentException("Exception: No applications are available for this task")
            } else return result

        } else throw ResourceForbiddenException("Exception: You do not have permission to view the applications for this task")
    }

    fun getApplicationsForUser(page: Int, size: Int): List<Application> {
        val user = userRepository.findByEmail(authenticationDetails.getEmail())

        val result = appRepository.findAllByUser(createPageRequest(page, size), user).content

        if (result.isEmpty()) {
            throw NoContentException("Exception: No applications are available for this user")
        } else return result
    }

    private fun createPageRequest(page: Int, size: Int): Pageable {
        return PageRequest.of(page, size)
    }

}
