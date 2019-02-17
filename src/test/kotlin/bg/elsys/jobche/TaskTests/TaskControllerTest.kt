package bg.elsys.jobche.TaskTests

import bg.elsys.jobche.BaseUnitTest
import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.controller.TaskController
import bg.elsys.jobche.service.ApplicationService
import bg.elsys.jobche.service.TaskService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.springframework.http.HttpStatus

class TaskControllerTest : BaseUnitTest() {

    companion object {
        val task = DefaultValues.task
        val tasks = listOf(task, task)
        val taskBody = DefaultValues.taskBody
        val taskResponse = DefaultValues.taskResponse
        val taskPaginatedResponse = DefaultValues.taskPaginatedResponse
    }

    private val taskService: TaskService = mockk()
    private val applicationService: ApplicationService = mockk()

    private val controller: TaskController

    init {
        controller = TaskController(taskService, applicationService)
    }

    @Test
    fun `create task`() {
        every { taskService.create(taskBody) } returns task

        val result = controller.create(taskBody)

        assertThat(result.body).isEqualTo(taskResponse)
    }


    @Nested
    inner class read {
        @Test
        fun `read one task`() {
            every { taskService.read(anyLong()) } returns task

            val result = controller.read(anyLong())

            assertThat(result.body).isEqualTo(taskResponse)
        }

        @Test
        fun `read multiple tasks paginated without filtering`() {
            every { taskService.readPaginated(anyInt(), anyInt()) } returns tasks

            val result = controller.readPaginated(anyInt(), anyInt())

            assertThat(result.body).isEqualTo(taskPaginatedResponse)
        }

        @Test
        fun `read my tasks paginated`() {
            every { taskService.readMePaginated(anyInt(), anyInt()) } returns tasks

            val result = controller.readMePaginated(anyInt(), anyInt())

            assertThat(result.body).isEqualTo(taskPaginatedResponse)
        }

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

    @Test
    fun `get applications for task`() {
        every { applicationService.getApplicationsForTask(task.id, 1, 1) } returns
                listOf(DefaultValues.application)

        val result = controller.getApplications(task.id, 1, 1)

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body?.applications).isEqualTo(listOf(DefaultValues.applicationResponse))
    }
}