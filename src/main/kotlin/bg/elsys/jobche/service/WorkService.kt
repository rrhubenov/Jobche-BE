package bg.elsys.jobche.service

import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.converter.Converters
import bg.elsys.jobche.entity.body.WorkBody
import bg.elsys.jobche.entity.model.task.Application
import bg.elsys.jobche.entity.model.user.User
import bg.elsys.jobche.entity.model.work.Participation
import bg.elsys.jobche.entity.model.work.Work
import bg.elsys.jobche.entity.model.work.WorkStatus
import bg.elsys.jobche.entity.response.WorkResponse
import bg.elsys.jobche.exception.ResourceForbiddenException
import bg.elsys.jobche.exception.ResourceNotFoundException
import bg.elsys.jobche.exception.UserNotFoundException
import bg.elsys.jobche.repository.ParticipationRepository
import bg.elsys.jobche.repository.TaskRepository
import bg.elsys.jobche.repository.UserRepository
import bg.elsys.jobche.repository.WorkRepository
import org.springframework.stereotype.Service

@Service
class WorkService(private val workRepository: WorkRepository,
                  private val participationRepository: ParticipationRepository,
                  private val taskRepository: TaskRepository,
                  private val userRepository: UserRepository,
                  private val authenticationDetails: AuthenticationDetails,
                  private val converters: Converters) {
    fun create(workBody: WorkBody): WorkResponse {
        val optionalTask = taskRepository.findById(workBody.taskId)
        val requestingUser = authenticationDetails.getUser()

        //Check if the task exists
        if (optionalTask.isPresent) {
            val task = optionalTask.get()

            //Check if the requesting user is the creator of the task
            if (requestingUser.id == task.creator.id) {
                val users = userRepository.findAllById(workBody.workers)

                if (users.size != workBody.workers.size) {
                    throw UserNotFoundException()
                }

                users.forEach { checkIfAcceptedForTask(it, task.applications) }

                val work = workRepository.save(Work(task))

                users.forEach { participationRepository.save(Participation(work, it))}

                with(converters) {
                    return work.response
                }


            } else throw ResourceForbiddenException("Exception: You are not the owner of the task")
        } else throw ResourceNotFoundException("Exception: No task with the following id was found")
    }

    fun delete(id: Long) {
        val user = userRepository.findByEmail(authenticationDetails.getEmail())
        val work = workRepository.findById(id)

        if (work.isPresent) {
            val creatorId = work.get().task.creator.id
            if (user?.id == creatorId) {
                workRepository.deleteById(id)
            } else throw ResourceForbiddenException("Exception: You are not the creator of this task")
        } else throw ResourceNotFoundException("Exception: No work with the following id was found")
    }

    fun read(id: Long): WorkResponse {
        val optional = workRepository.findById(id)

        if (optional.isPresent) {
            val work = optional.get()
            val requestingUser = authenticationDetails.getUser()

            //Check if the requesting user is the creator of the task or is one of the workers
            if (work.participations.stream().anyMatch { it.user.id == requestingUser.id }
                    || requestingUser.id == work.task.creator.id) {

                with(converters) {
                    return work.response
                }

            } else throw ResourceForbiddenException("Exception: You do not have permission to view this resource")

        } else throw ResourceNotFoundException("Exception: No work with the following id was found")
    }

    fun changeStatus(workStatus: WorkStatus, id: Long) {
        if (workRepository.existsById(id)) {
            val user = userRepository.findByEmail(authenticationDetails.getEmail())

            val work = workRepository.getOne(id)

            if (work.task.creator.id == user?.id) {
                work.status = workStatus
                workRepository.save(work)
            } else throw ResourceForbiddenException("Exception: You do not have permission to modify the following resource")

        } else throw ResourceNotFoundException("Exception: No work with the following id was found")
    }

    private fun checkIfAcceptedForTask(user: User, applications: List<Application>) {
        if(!applications.any { it.user.id == user.id && it.accepted}) {
            throw ResourceForbiddenException("Exception: A participant was not part of the application list or is not accepted")
        }
    }
}
