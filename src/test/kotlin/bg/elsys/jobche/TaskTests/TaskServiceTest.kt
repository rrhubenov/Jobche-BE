package bg.elsys.jobche.TaskTests

import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.model.Task
import bg.elsys.jobche.entity.response.TaskResponse
import bg.elsys.jobche.repositories.TaskRepository
import bg.elsys.jobche.service.TaskService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
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
        private val task = Task(TITLE, DESCRIPTION, PAYMENT, NUMBER_OF_WORKERS, DATE_TIME)
        private val taskBody = TaskBody(TITLE, PAYMENT, NUMBER_OF_WORKERS, DESCRIPTION, DATE_TIME)
        private val taskResponse = TaskResponse(anyLong(),
                taskBody.title,
                taskBody.description,
                taskBody.payment,
                taskBody.numberOfWorkers,
                taskBody.dateTime)
    }

    private val repository: TaskRepository = mockk()

    private val taskService: TaskService

    init {
        taskService = TaskService(repository)
    }

    @Test
    fun `create task`() {
        every { repository.save(any<Task>()) } returns task
        val response = taskService.create(taskBody)
        assertThat(response).isEqualTo(taskResponse)
    }

    @Test
    fun `read task`() {
        every { repository.existsById(anyLong()) } returns true
        every { repository.findById(anyLong()) } returns Optional.of(task)
        val response = taskService.read(anyLong())
        assertThat(response).isEqualTo(taskResponse)
    }

    @Test
    fun `update task`() {
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

    @Test
    fun `delete task`() {
        every { repository.existsById(anyLong()) } returns true
        every { repository.deleteById(anyLong()) } returns Unit

        taskService.delete(anyLong())

        verify {
            repository.existsById(anyLong())
            repository.deleteById(anyLong())
        }
    }
}