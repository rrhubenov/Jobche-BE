package bg.elsys.jobche.WorkTests

import bg.elsys.jobche.BaseUnitTest
import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.controller.WorkController
import bg.elsys.jobche.entity.model.work.WorkStatus
import bg.elsys.jobche.service.WorkService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class WorkControllerTest: BaseUnitTest() {


    companion object {
        val work = DefaultValues.work
        val workBody = DefaultValues.workBody
        val workResponse = DefaultValues.workResponse
    }

    val service: WorkService = mockk()
    val controller = WorkController(service)

    @Test
    fun `create work should return 201 and proper response body`() {
        every { service.create(workBody) } returns workResponse

        val response = controller.create(workBody)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body).isEqualToComparingFieldByFieldRecursively(workResponse)
    }

    @Test
    fun `delete work should return 200`() {
        every { service.delete(work.id) } returns Unit

        val response = controller.delete(work.id)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `read should return 200 and the response body`() {
        every { service.read(work.id) } returns workResponse

        val response = controller.read(work.id)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualToComparingFieldByFieldRecursively(workResponse)
    }

    @Test
    fun `ending work should return 200`() {
        every { service.end(WorkStatus.ENDED, work.id) } returns Unit

        val response = controller.end(WorkStatus.ENDED, work.id)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

}