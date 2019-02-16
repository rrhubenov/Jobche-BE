package bg.elsys.jobche.WorkTests

import bg.elsys.jobche.BaseUnitTest
import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.repository.ParticipationRepository
import bg.elsys.jobche.repository.TaskRepository
import bg.elsys.jobche.repository.UserRepository
import bg.elsys.jobche.repository.WorkRepository
import bg.elsys.jobche.service.WorkService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import java.util.*

class WorkServiceTest : BaseUnitTest() {

    companion object {
        val work = DefaultValues.work
        val workBody = DefaultValues.workBody
        val workResponse = DefaultValues.workResponse

        //Task constants
        val task = DefaultValues.task

        //User constants
        val user = DefaultValues.user

        //Participation constants
        val participation = DefaultValues.participation
    }

    val workRepository: WorkRepository = mockk()
    val participationRepository: ParticipationRepository = mockk()
    val taskRepository: TaskRepository = mockk()
    val userRepository: UserRepository = mockk()
    val authenticationDetails: AuthenticationDetails = mockk()
    val service = WorkService(workRepository, participationRepository, taskRepository, userRepository, authenticationDetails)

    @Test
    fun `create should call repo methods and return the created work`() {
        every { taskRepository.findById(workBody.taskId) } returns Optional.of(task)
        every { userRepository.findAllById(workBody.workers) } returns listOf(user)
        every { workRepository.save(work) } returns work
        every { participationRepository.save(participation) } returns participation

        val result = service.create(workBody)

        verify {
            taskRepository.findById(workBody.taskId)
            userRepository.findAllById(workBody.workers)
            workRepository.save(work)
            participationRepository.save(participation)
        }

        assertThat(result).isEqualToComparingFieldByFieldRecursively(workResponse)
    }

    @Test
    fun `delete should run repo methods`() {
        every { authenticationDetails.getEmail() } returns anyString()
        every { userRepository.findByEmail(anyString()) } returns user
        every { workRepository.findById(work.id) } returns Optional.of(work)
        every { workRepository.deleteById(work.id) } returns Unit

        service.delete(work.id)

        verify {
            authenticationDetails.getEmail()
            userRepository.findByEmail(any())
            workRepository.findById(work.id)
            workRepository.deleteById(work.id)
        }
    }
}