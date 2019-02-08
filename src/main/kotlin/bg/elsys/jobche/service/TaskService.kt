package bg.elsys.jobche.service

import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.model.task.Task
import bg.elsys.jobche.exception.TaskModificationForbiddenException
import bg.elsys.jobche.exception.TaskNotFoundException
import bg.elsys.jobche.repository.TaskRepository
import bg.elsys.jobche.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TaskService(val taskRepository: TaskRepository,
                  val userRepository: UserRepository,
                  val authenticationDetails: AuthenticationDetails) {
    fun create(taskBody: TaskBody): Task {
        //Get Id of the creator
        val user = userRepository.findByEmail(authenticationDetails.getEmail())

        return taskRepository.save(Task(taskBody.title,
                taskBody.description,
                taskBody.payment,
                taskBody.numberOfWorkers,
                taskBody.dateTime,
                user!!.id,
                taskBody.location
        ))
    }

    fun read(id: Long): Task {
        if (taskRepository.existsById(id)) {
            return taskRepository.findById(id).get()
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

    fun readPaginated(page: Int, size: Int,
                      title: String? = null,
                      paymentStart: Int? = null,
                      paymentEnd: Int? = null,
                      numWStart: Int? = null,
                      numWEnd: Int? = null,
                      dateStart: LocalDateTime? = null,
                      dateEnd: LocalDateTime? = null,
                      city: String? = null): List<Task> {
        return taskRepository.findAll(createPageRequest(page, size), title, paymentStart, paymentEnd, numWStart, numWEnd, dateStart, dateEnd, city)
    }


    fun readMePaginated(page: Int, size: Int): List<Task> {
        val user = userRepository.findByEmail(authenticationDetails.getEmail())

        return taskRepository.findAllByCreatorId(createPageRequest(page, size), user?.id).content
    }

    private fun createPageRequest(page: Int, size: Int): Pageable {
        return PageRequest.of(page, size)
    }

}
