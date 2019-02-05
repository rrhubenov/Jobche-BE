package bg.elsys.jobche.TaskTests

import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.entity.model.task.Task
import bg.elsys.jobche.repository.TaskRepository
import bg.elsys.jobche.repository.UserRepository
import bg.elsys.jobche.service.TaskService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*

@ExtendWith(MockKExtension::class)
class TaskServiceTest {

    companion object {
        private val user = DefaultValues.user
        private val task = DefaultValues.task
        private val taskBody = DefaultValues.taskBody
        private val taskResponse = DefaultValues.taskResponse
    }

    private val repository: TaskRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val authenticationDetails: AuthenticationDetails = mockk()

    private val taskService: TaskService

    init {
        taskService = TaskService(repository, userRepository, authenticationDetails)
    }

    @Test
    fun `create task`() {
        every { authenticationDetails.getEmail() } returns user.email
        every { userRepository.findByEmail(user.email) } returns user
        every { repository.save(any<Task>()) } returns task

        val result = taskService.create(taskBody)
        assertThat(result).isEqualTo(task)
    }

    @Nested
    inner class read {
        @Test
        fun `read one task`() {
            every { repository.existsById(anyLong()) } returns true
            every { repository.findById(anyLong()) } returns Optional.of(task)
            val result = taskService.read(anyLong())
            assertThat(result).isEqualTo(task)
        }

        @Test
        fun `read tasks paginated without filter`() {
            val tasks = listOf(task, task)

            every { repository.findAll(any<Pageable>()) } returns PageImpl<Task>(tasks)
            val result = taskService.readPaginated(1, 1, null)

            assertThat(result).isEqualTo(tasks)
        }

        @Test
        fun `read my tasks paginated`() {
            val tasks = listOf(task, task)

            every { authenticationDetails.getEmail() } returns anyString()
            every { userRepository.findByEmail(anyString()) } returns user
            every { repository.findAllByCreatorId(any<Pageable>(), user.id) } returns PageImpl<Task>(tasks)

            val result = taskService.readMePaginated(1, 1)

            assertThat(result).isEqualTo(tasks)
        }
    }

    @Nested
    inner class update {
        @Test
        fun `update task with valid user`() {
            every { authenticationDetails.getEmail() } returns user.email
            every { userRepository.findByEmail(user.email) } returns user
            every { repository.existsById(anyLong()) } returns true
            every { repository.getOne(anyLong()) } returns task
            every { repository.save(any<Task>()) } returns task

            taskService.update(taskBody, anyLong())

            verify {
                repository.existsById(anyLong())
                repository.getOne(anyLong())
                repository.save(any<Task>())
            }
        }
    }


    @Nested
    inner class delete {

        @Test
        fun `delete task`() {
            every { repository.existsById(anyLong()) } returns true
            every { authenticationDetails.getEmail() } returns user.email
            every { userRepository.findByEmail(user.email) } returns user
            every { repository.findById(anyLong()) } returns Optional.of(task)
            every { repository.deleteById(anyLong()) } returns Unit

            taskService.delete(anyLong())

            verify {
                repository.existsById(anyLong())
                userRepository.findByEmail(anyString())
                repository.findById(anyLong())
                repository.deleteById(anyLong())
            }
        }
    }
}