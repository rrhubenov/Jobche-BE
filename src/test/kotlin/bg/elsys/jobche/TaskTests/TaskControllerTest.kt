package bg.elsys.jobche.TaskTests

import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.controller.TaskController
import bg.elsys.jobche.entity.body.task.Address
import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.model.Application
import bg.elsys.jobche.entity.model.Task
import bg.elsys.jobche.entity.model.User
import bg.elsys.jobche.entity.response.application.ApplicationResponse
import bg.elsys.jobche.entity.response.task.TaskPaginatedResponse
import bg.elsys.jobche.entity.response.task.TaskResponse
import bg.elsys.jobche.service.ApplicationService
import bg.elsys.jobche.service.TaskService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.*
import org.springframework.http.HttpStatus
import sun.management.snmp.AdaptorBootstrap
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class TaskControllerTest {

    companion object {
        const val TITLE = "Test Title"
        const val PAYMENT = 10
        const val NUMBER_OF_WORKERS = 1
        const val DESCRIPTION = "Test Description"
        val LOCATION = Address(anyString(), anyString(), anyString())
        val DATE_TIME = LocalDateTime.now()
        const val CREATOR_ID = 1L
        val task = Task(TITLE, DESCRIPTION, PAYMENT, NUMBER_OF_WORKERS, DATE_TIME,CREATOR_ID, LOCATION)
        val tasks = listOf(task, task)
        val taskBody = TaskBody(TITLE, PAYMENT, NUMBER_OF_WORKERS, DESCRIPTION, DATE_TIME, LOCATION)
        val taskResponse = TaskResponse(task.id, TITLE, DESCRIPTION, PAYMENT, NUMBER_OF_WORKERS, DATE_TIME, LOCATION, CREATOR_ID)
        val taskPaginatedResponse = TaskPaginatedResponse(listOf(taskResponse, taskResponse))
    }

    private val taskService : TaskService = mockk()
    private val applicationService: ApplicationService = mockk()

    private val controller: TaskController

    init {
        controller = TaskController(taskService, applicationService)
    }

    @Test
    fun `create task`() {
        every { taskService.create(taskBody) } returns taskResponse

        val result = controller.create(taskBody)

        assertThat(result.body).isEqualTo(taskResponse)
    }


    @Nested
    inner class read {
        @Test
        fun `read one task`() {
            every { taskService.read(anyLong()) } returns taskResponse

            val result = controller.read(anyLong())

            assertThat(result.body).isEqualTo(taskResponse)
        }

        @Test
        fun `read multiple tasks paginated`() {
            every { taskService.readPaginated(anyInt(), anyInt()) } returns tasks

            val result = controller.readPaginated(anyInt(), anyInt())

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