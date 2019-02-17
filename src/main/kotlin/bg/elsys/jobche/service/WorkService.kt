package bg.elsys.jobche.service

import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.entity.body.WorkBody
import bg.elsys.jobche.entity.model.work.Work
import bg.elsys.jobche.entity.model.work.Participation
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
                  private val authenticationDetails: AuthenticationDetails) {
    fun create(workBody: WorkBody): WorkResponse {
        val optional = taskRepository.findById(workBody.taskId)
        val requestingUser = userRepository.findByEmail(authenticationDetails.getEmail())

        //Check if the task exists
        if (optional.isPresent) {
            val task = optional.get()

            //Check if the requesting user is the creator of the task
            if(requestingUser?.id == task.creator.id) {
                val users = userRepository.findAllById(workBody.workers)

                if (users.size != workBody.workers.size) {
                    throw UserNotFoundException()
                }

                val work = workRepository.save(Work(task))

                users.forEach { participant ->
                    //Check if the participant is one of the accepted appliers
                    if(task.applications.stream().anyMatch { it.user.id == participant.id && it.accepted == true }) {
                        participationRepository.save(Participation(work, participant))
                    } else {
                        workRepository.deleteById(work.id)
                        throw ResourceForbiddenException()
                    }
                }

                return WorkResponse(work.id, task, users, work.createdAt, work.status)
            } else throw ResourceForbiddenException()
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

    fun read(id: Long): WorkResponse {
        val optional = workRepository.findById(id)

        if(optional.isPresent) {
            val work = optional.get()
            val requestingUser = userRepository.findByEmail(authenticationDetails.getEmail())

            //Check if the requesting user is the creator of the task or is one of the workers
            if(work.participations.stream().anyMatch {  it.user.id ==  requestingUser?.id }
                    || requestingUser?.id == work.task.creator.id) {

                return WorkResponse(work.id, work.task, work.participations.map { it.user }, work.createdAt, work.status)

            } else throw ResourceForbiddenException()

        } else throw ResourceNotFoundException()
    }

    fun changeStatus(workStatus: WorkStatus, id: Long) {
        if(workRepository.existsById(id)) {
            val user = userRepository.findByEmail(authenticationDetails.getEmail())

            val work = workRepository.getOne(id)

            if(work.task.creator.id == user?.id) {
                work.status = workStatus
                workRepository.save(work)
            } else throw ResourceForbiddenException()

        } else throw ResourceNotFoundException()
    }

}
