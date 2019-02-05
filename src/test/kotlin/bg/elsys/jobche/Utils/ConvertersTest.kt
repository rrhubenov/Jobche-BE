package bg.elsys.jobche.Utils

import bg.elsys.jobche.DefaultValues
import bg.elsys.jobche.converter.Converters
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConvertersTest {
    companion object {
        val taskResponse = DefaultValues.taskResponse
        val userResponse = DefaultValues.userResponse
        val applicationResponse = DefaultValues.applicationResponse
    }

    val converters = Converters()

    @Test
    fun `test task response convertion`() {
        val task = DefaultValues.task

        with(converters) {
            assertThat(task.response).isEqualTo(taskResponse)
        }
    }

    @Test
    fun `test user response convertion`() {
        val user = DefaultValues.user

        with(converters) {
            assertThat(user.response).isEqualTo(userResponse)
        }
    }

    @Test
    fun `test application response convertion`() {
        val application = DefaultValues.application

        with(converters) {
            assertThat(application.response).isEqualTo(applicationResponse)
        }
    }
}