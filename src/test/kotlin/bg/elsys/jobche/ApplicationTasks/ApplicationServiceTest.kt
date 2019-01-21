package bg.elsys.jobche.ApplicationTasks

import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.entity.body.application.ApplicationBody
import bg.elsys.jobche.entity.model.Application
import bg.elsys.jobche.repositories.ApplicationRepository
import bg.elsys.jobche.repositories.TaskRepository
import bg.elsys.jobche.repositories.UserRepository
import bg.elsys.jobche.service.ApplicationService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import java.util.*

@ExtendWith(MockKExtension::class)
class ApplicationServiceTest {
    companion object {
        val user = DefaultValues.user
        val task = DefaultValues.task
        val applicationBody = ApplicationBody(task.id)
        val application = Application(user, task)
    }

    val appRepository: ApplicationRepository = mockk()
    val userRepository: UserRepository = mockk()
    val taskRepository: TaskRepository = mockk()
    val authenticationDetails: AuthenticationDetails = mockk()

    val service: ApplicationService = ApplicationService(appRepository, userRepository, taskRepository, authenticationDetails)

    init {
        every {authenticationDetails.getEmail() } returns anyString()
    }

    @Nested
    inner class create {
        @Test
        fun `create application`() {
            every { taskRepository.existsById(task.id) } returns true
            every { userRepository.findByEmail(anyString()) } returns user
            every { taskRepository.findById(task.id) } returns Optional.of(task)
            every { appRepository.save(application) } returns application

            val result = service.create(applicationBody)

            assertThat(result).isEqualTo(application)

            verify {
                taskRepository.existsById(task.id)
                authenticationDetails.getEmail()
                userRepository.findByEmail(anyString())
                taskRepository.findById(task.id)
                appRepository.save(application)
            }
        }
    }

    @Nested
    inner class remove {
        @Test
        fun `successfully remove application`() {
            every { appRepository.existsById(anyLong()) } returns true
            every { userRepository.findByEmail(anyString()) } returns user
            every { appRepository.findById(anyLong()) } returns Optional.of(application)
            every { appRepository.deleteById(anyLong()) } returns Unit

            service.delete(anyLong())

            verify {
                appRepository.existsById(anyLong())
                authenticationDetails.getEmail()
                userRepository.findByEmail(anyString())
                appRepository.findById(anyLong())
                appRepository.deleteById(anyLong())
            }
        }
    }

    @Nested
    inner class approve() {
        @Test
        fun `successfully approve an applicaiton`() {
            every { appRepository.existsById(anyLong()) } returns true
            every { authenticationDetails.getEmail() } returns anyString()
            every { userRepository.findByEmail(anyString()) } returns user
            every { appRepository.getOne(anyLong()) } returns application
            every { appRepository.save(any<Application>()) } returns application

            service.approveApplication(anyLong())

            verify {
                appRepository.existsById(anyLong())
                authenticationDetails.getEmail()
                userRepository.findByEmail(anyString())
                appRepository.getOne(anyLong())
                appRepository.save(application)
            }
        }
    }
}