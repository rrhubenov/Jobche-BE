package bg.elsys.jobche.Utils

import bg.elsys.jobche.entity.body.user.DateOfBirth
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DateOfBirthTests {
    @Test
    fun `test toString method`() {
        val dateOfBirth = DateOfBirth(1, 1, 2000)

        assertThat(dateOfBirth.toString()).isEqualTo("1-1-2000")
    }
}