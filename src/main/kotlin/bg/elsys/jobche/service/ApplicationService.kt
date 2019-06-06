package bg.elsys.jobche.service

import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.entity.body.application.ApplicationBody
import bg.elsys.jobche.entity.model.task.Application
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
        val optionalTask = taskRepository.findById(application.taskId)

        if (optionalTask.isPresent) {

            val task = optionalTask.get()
            val user = authenticationDetails.getUser()
            return appRepository.save(Application(user, task))

        } else throw TaskNotFoundException()
    }

    fun delete(id: Long) {
        val app = getApp(id)
        val user = authenticationDetails.getUser()

        if (app.user.id == user.id) {

            appRepository.deleteById(id)

        } else throw ResourceForbiddenException("Exception: You do not have permission to modify this application")
    }

    fun changeApplicationStatus(id: Long, value: Boolean) {
        val app = getApp(id)
        val user = authenticationDetails.getUser()
        if (app.task?.creator?.id == user.id) {

            if(app.accepted == value) {
                throw ResourceForbiddenException("The wanted value has already been applied to the following application")
            }

            app.accepted = value
            if (value == true) {
                if (app.task.acceptedWorkersCount < app.task.numberOfWorkers) {
                    app.task.acceptedWorkersCount++
                } else throw ResourceForbiddenException("Max applications are already approved. Disapprove an application to approve another one.")
            } else if(app.task.acceptedWorkersCount > 0){
                app.task.acceptedWorkersCount--
            } else throw ResourceForbiddenException("No more applications to disapprove")
            appRepository.save(app)
        } else throw ResourceForbiddenException("Exception: You do not have permission to modify this application")
    }

    fun getApplicationsForTask(taskId: Long, page: Int, size: Int): List<Application> {
        val user = authenticationDetails.getUser()
        val optionalTask = taskRepository.findById(taskId)

        if (optionalTask.isPresent) {
            val task = optionalTask.get()
            if (task.creator.id == user.id) {
                val result = appRepository.findAllByTask(createPageRequest(page, size), task).content

                if (!result.isEmpty()) {
                    return result
                } else throw NoContentException("Exception: No applications are available for this task")

            } else throw ResourceForbiddenException("Exception: You do not have permission to view the applications for this task")
        } else throw ResourceNotFoundException()
    }

    fun getApplicationsForUser(page: Int, size: Int): List<Application> {
        val user = userRepository.findByEmail(authenticationDetails.getEmail())

        val result = appRepository.findAllByUser(createPageRequest(page, size), user).content

        if (!result.isEmpty()) {
           return result
        } else throw NoContentException("Exception: No applications are available for this user")
    }

    private fun createPageRequest(page: Int, size: Int): Pageable {
        return PageRequest.of(page, size)
    }

    private fun getApp(id: Long): Application {
        val optionalApp = appRepository.findById(id)

        if (optionalApp.isPresent) return optionalApp.get()
        else throw ResourceNotFoundException("Exception: No application matching this id was found")
    }

}
