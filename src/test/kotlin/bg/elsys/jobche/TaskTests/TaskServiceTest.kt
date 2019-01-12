package bg.elsys.jobche.TaskTests

import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.model.Task
import bg.elsys.jobche.entity.model.User
import bg.elsys.jobche.entity.response.TaskPaginatedResponse
import bg.elsys.jobche.entity.response.TaskResponse
import bg.elsys.jobche.repositories.TaskRepository
import bg.elsys.jobche.repositories.UserRepository
import bg.elsys.jobche.service.TaskService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockKExtension::class)
class TaskServiceTest {

    companion object {
        private const val TITLE = "Test Title"
        private const val PAYMENT = 10
        private const val NUMBER_OF_WORKERS = 2
        private const val DESCRIPTION = "Test Description"
        private val DATE_TIME = LocalDateTime.now()
        private val user = User(anyString(), anyString(), anyString(), anyString())
        private val task = Task(TITLE, DESCRIPTION, PAYMENT, NUMBER_OF_WORKERS, DATE_TIME, user.id)
        private val taskBody = TaskBody(TITLE, PAYMENT, NUMBER_OF_WORKERS, DESCRIPTION, DATE_TIME)
        private val taskResponse = TaskResponse(anyLong(),
                taskBody.title,
                taskBody.description,
                taskBody.payment,
                taskBody.numberOfWorkers,
                taskBody.dateTime)
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
        every { userRepository.findByEmail(anyString()) } returns user
        every { repository.save(any<Task>()) } returns task

        val response = taskService.create(taskBody)
        assertThat(response).isEqualTo(taskResponse)
    }

    @Nested
    inner class read {
        @Test
        fun `read one task`() {
            every { repository.existsById(anyLong()) } returns true
            every { repository.findById(anyLong()) } returns Optional.of(task)
            val response = taskService.read(anyLong())
            assertThat(response).isEqualTo(taskResponse)
        }

        @Test
        fun `read tasks paginated`() {
            val tasks = listOf(task, task)

            every { repository.findAll(any<Pageable>()) } returns PageImpl<Task>(tasks)
            val response = taskService.readPaginated(1, 1)

            assertThat(response).isEqualTo(tasks)
        }
    }

    @Nested
    inner class update {
        @Test
        fun `update task with valid user`() {
            every { authenticationDetails.getEmail() } returns user.email
            every { userRepository.findByEmail(anyString()) } returns user
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
            every { userRepository.findByEmail(anyString()) } returns user
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