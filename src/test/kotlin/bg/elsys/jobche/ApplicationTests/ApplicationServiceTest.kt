package bg.elsys.jobche.ApplicationTests

import bg.elsys.jobche.BaseUnitTest
import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.entity.model.Application
import bg.elsys.jobche.repository.ApplicationRepository
import bg.elsys.jobche.repository.TaskRepository
import bg.elsys.jobche.repository.UserRepository
import bg.elsys.jobche.service.ApplicationService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*

class ApplicationServiceTest : BaseUnitTest() {
    companion object {
        val user = DefaultValues.user
        val task = DefaultValues.task
        val applicationBody = DefaultValues.applicationBody
        val application = DefaultValues.application
        val applications = listOf(application, application)
    }

    val appRepository: ApplicationRepository = mockk()
    val userRepository: UserRepository = mockk()
    val taskRepository: TaskRepository = mockk()
    val authenticationDetails: AuthenticationDetails = mockk()

    val service: ApplicationService = ApplicationService(appRepository, userRepository, taskRepository, authenticationDetails)

    init {
        every { authenticationDetails.getEmail() } returns anyString()
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

    @Nested
    inner class read {
        @Test
        fun `get applications for task`() {
            every { authenticationDetails.getEmail() } returns anyString()
            every { userRepository.findByEmail(anyString()) } returns user
            every { taskRepository.existsById(task.id) } returns true
            every { taskRepository.findById(task.id) } returns Optional.of(task)
            every { appRepository.findAll(any<Pageable>()) } returns PageImpl<Application>(applications)

            val result = service.getApplicationsForTask(task.id, 1, 1)

            verify {
                authenticationDetails.getEmail()
                userRepository.findByEmail(anyString())
                taskRepository.existsById(task.id)
                taskRepository.findById(task.id)
                appRepository.findAll(any<Pageable>())
            }

            assertThat(result).isEqualTo(applications)
        }

        @Test
        fun `get applications from user`() {
            every { authenticationDetails.getEmail() } returns anyString()
            every { userRepository.findByEmail(anyString()) } returns user
            every { appRepository.findAllByUser(any<Pageable>(), user) } returns PageImpl<Application>(applications)

            val result = service.getApplicationsForUser(1, 1)

            verify {
                authenticationDetails.getEmail()
                userRepository.findByEmail(anyString())
                appRepository.findAllByUser(any<Pageable>(), user)
            }

            assertThat(result).isEqualTo(applications)
        }
    }
}