package bg.elsys.jobche.service

import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.entity.body.WorkBody
import bg.elsys.jobche.entity.model.work.Work
import bg.elsys.jobche.entity.model.work.Participation
import bg.elsys.jobche.entity.response.WorkResponse
import bg.elsys.jobche.exception.ResourceForbiddenException
import bg.elsys.jobche.exception.ResourceNotFoundException
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
                  private val authenticationDetails: AuthenticationDetails) {
    fun create(workBody: WorkBody): WorkResponse {
        val task = taskRepository.findById(workBody.taskId)
        if (task.isPresent) {
            val users = userRepository.findAllById(workBody.workers)

            val work = workRepository.save(Work(task.get()))

            users.forEach { participationRepository.save(Participation(work, it)) }

            return WorkResponse(work.id, task.get(), users, work.createdAt)
        } else throw ResourceNotFoundException()
    }

    fun delete(id: Long) {
        val user = userRepository.findByEmail(authenticationDetails.getEmail())
        val work = workRepository.findById(id)

        if (work.isPresent) {
            val creatorId = work.get().task.creator.id
            if (user?.id == creatorId) {
                workRepository.deleteById(id)
            } else throw ResourceForbiddenException()
        } else throw ResourceNotFoundException()
    }

}
