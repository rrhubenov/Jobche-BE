package bg.elsys.jobche.TaskTests

import bg.elsys.jobche.controller.TaskController
import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.response.TaskResponse
import bg.elsys.jobche.service.TaskService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class TaskControllerTest {

    companion object {
        const val TITLE = "Test Title"
        const val PAYMENT = 10
        const val NUMBER_OF_WORKERS = 1
        const val DESCRIPTION = "Test Description"
        val DATE_TIME = LocalDateTime.now()
        val taskResponse = TaskResponse(anyLong(), TITLE, DESCRIPTION, PAYMENT, NUMBER_OF_WORKERS, DATE_TIME)
        val taskBody = TaskBody(TITLE, PAYMENT, NUMBER_OF_WORKERS, DESCRIPTION, DATE_TIME)
    }

    private val taskService : TaskService = mockk()

    private val controller: TaskController

    init {
        controller = TaskController(taskService)
    }

    @Test
    fun `create task`() {
        every { taskService.create(taskBody) } returns taskResponse

        val result = controller.create(taskBody)

        assertThat(result.body).isEqualTo(taskResponse)
    }

    @Test
    fun `read task`() {
        every { taskService.read(anyLong()) } returns taskResponse

        val result = controller.read(anyLong())

        assertThat(result.body).isEqualTo(taskResponse)
    }

    @Test
    fun `update task`() {
        every { taskService.update(taskBody, anyLong()) } returns Unit

        val result = controller.update(anyLong(), taskBody)

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `delete task`() {
        every { taskService.delete(anyLong()) } returns Unit

        val result = controller.delete(anyLong())

        assertThat(result.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }
}