package bg.elsys.jobche.service

import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.model.task.Task
import bg.elsys.jobche.entity.response.task.TaskResponse
import bg.elsys.jobche.exception.TaskModificationForbiddenException
import bg.elsys.jobche.exception.TaskNotFoundException
import bg.elsys.jobche.repository.TaskRepository
import bg.elsys.jobche.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TaskService(val taskRepository: TaskRepository,
                  val userRepository: UserRepository,
                  val authenticationDetails: AuthenticationDetails) {
    fun create(taskBody: TaskBody): TaskResponse {
        //Get Id of the creator
        val user = userRepository.findByEmail(authenticationDetails.getEmail())

        val task = taskRepository.save(Task(taskBody.title,
                taskBody.description,
                taskBody.payment,
                taskBody.numberOfWorkers,
                taskBody.dateTime,
                user!!.id,
                taskBody.location,
                taskBody.paymentType
        ))

        return TaskResponse(task.id,
                task.title,
                task.description,
                task.payment,
                task.numberOfWorkers,
                task.dateTime,
                task.location,
                task.creatorId,
                task.paymentType
        )
    }

    fun read(id: Long): TaskResponse {
        if (taskRepository.existsById(id)) {
            val task = taskRepository.findById(id).get()
            return TaskResponse(task.id,
                    task.title,
                    task.description,
                    task.payment,
                    task.numberOfWorkers,
                    task.dateTime,
                    task.location,
                    task.creatorId,
                    task.paymentType
            )
        } else throw TaskNotFoundException()
    }

    fun update(task: TaskBody, id: Long) {
        if (taskRepository.existsById(id)) {
            val user = userRepository.findByEmail(authenticationDetails.getEmail())
            val taskToUpdate = taskRepository.getOne(id)

            if (taskToUpdate.creatorId != user?.id) {
                throw TaskModificationForbiddenException()
            }

            taskToUpdate.title = task.title
            taskToUpdate.payment = task.payment
            taskToUpdate.numberOfWorkers = task.numberOfWorkers
            taskToUpdate.description = task.description
            taskToUpdate.dateTime = task.dateTime
            taskRepository.save(taskToUpdate)

        } else throw TaskNotFoundException()
    }

    fun delete(id: Long) {
        if (taskRepository.existsById(id)) {
            val task = taskRepository.findById(id)
            val user = userRepository.findByEmail(authenticationDetails.getEmail())

            if (task.get().creatorId != user!!.id) {
                throw TaskModificationForbiddenException()
            }

            taskRepository.deleteById(id)
        } else throw TaskNotFoundException()
    }

    fun readPaginated(page: Int, size: Int): List<Task> {
        return taskRepository.findAll(createPageRequest(page, size)).content
    }

    fun readMePaginated(page: Int, size: Int): List<Task> {
        val user = userRepository.findByEmail(authenticationDetails.getEmail())

        return taskRepository.findAllByCreatorId(createPageRequest(page, size), user?.id).content
    }

    private fun createPageRequest(page: Int, size: Int): Pageable {
        return PageRequest.of(page, size)
    }

}
