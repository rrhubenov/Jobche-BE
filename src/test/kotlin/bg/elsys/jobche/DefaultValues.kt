package bg.elsys.jobche

import bg.elsys.jobche.entity.body.task.Address
import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.entity.model.Application
import bg.elsys.jobche.entity.model.Task
import bg.elsys.jobche.entity.model.User
import bg.elsys.jobche.entity.response.application.ApplicationResponse
import java.time.LocalDateTime

class DefaultValues {
    companion object {
        //Task
        private const val TITLE = "Test Title"
        private const val PAYMENT = 10
        private const val NUMBER_OF_WORKERS = 1
        private const val DESCRIPTION = "Test Description"
        private const val COUNTRY = "Bulgaria"
        private const val CITY = "Sofia"
        private const val NEIGHBORHOOD = "Krasno selo"
        private val LOCATION = Address(COUNTRY, CITY, NEIGHBORHOOD)
        private val DATE_TIME = LocalDateTime.now()
        private const val CREATOR_ID = 1L
        val task = Task(TITLE, DESCRIPTION, PAYMENT, NUMBER_OF_WORKERS, DATE_TIME, CREATOR_ID, LOCATION)

        //User
        private const val FIRST_NAME = "Radoslav"
        private const val LAST_NAME = "Hubenov"
        private const val EMAIL = "rrhubenov@gmail.com"
        private const val PASSWORD = "password"
        private val DATE_OF_BIRTH = DateOfBirth(1,1,2000)
        val user = User(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, DATE_OF_BIRTH.toString())

        //Application
        private const val ACCEPTED = false
        val application = Application(user, task, ACCEPTED)
        val applicationResponse = ApplicationResponse(application.id, application.user.id, application.task.id, application.accepted)
    }
}