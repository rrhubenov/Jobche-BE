package bg.elsys.jobche.TaskTests

import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.model.Task
import bg.elsys.jobche.entity.response.TaskResponse
import bg.elsys.jobche.repositories.TaskRepository
import bg.elsys.jobche.service.TaskService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskServiceTest {

    companion object {
        private const val TITLE = "Test Title"
        private const val PAYMENT = 10
        private const val NUMBER_OF_WORKERS = 2
        private const val DESCRIPTION = "Test Description"
        private val DATE_TIME = LocalDateTime.now()
        private val task = Task(TITLE, DESCRIPTION, PAYMENT, NUMBER_OF_WORKERS, DATE_TIME)
        private val taskBody = TaskBody(TITLE, PAYMENT, NUMBER_OF_WORKERS, DESCRIPTION, DATE_TIME)
    }

    private val repository: TaskRepository = mockk()

    private val taskService: TaskService

    init {
        taskService = TaskService(repository)
    }

    @Test
    fun createTask() {
        every { repository.save(any<Task>()) } returns task
        val taskResponse = taskService.createTask(taskBody)
        assertThat(taskResponse).isEqualTo(TaskResponse(anyLong(),
                taskBody.title,
                taskBody.description,
                taskBody.payment,
                taskBody.numberOfWorkers,
                taskBody.dateTime))
    }
}