package bg.elsys.jobche

import bg.elsys.jobche.entity.body.application.ApplicationBody
import bg.elsys.jobche.entity.body.task.Address
import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.entity.body.user.UserLoginBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.model.Application
import bg.elsys.jobche.entity.model.task.Task
import bg.elsys.jobche.entity.model.User
import bg.elsys.jobche.entity.model.task.PaymentType
import bg.elsys.jobche.entity.response.application.ApplicationResponse
import bg.elsys.jobche.entity.response.task.TaskPaginatedResponse
import bg.elsys.jobche.entity.response.task.TaskResponse
import bg.elsys.jobche.entity.response.user.UserResponse
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
        private val LOCATION = Address(COUNTRY, CITY)
        private val DATE_TIME = LocalDateTime.now()
        private const val CREATOR_ID = 1L
        private val PAYMENT_TYPE = PaymentType.BY_HOUR
        val task = Task(TITLE, DESCRIPTION, PAYMENT, NUMBER_OF_WORKERS, DATE_TIME, CREATOR_ID, LOCATION, PAYMENT_TYPE)
        val taskBody = TaskBody(TITLE, PAYMENT, NUMBER_OF_WORKERS, DESCRIPTION, DATE_TIME, LOCATION, PAYMENT_TYPE)
        val taskResponse = TaskResponse(task.id, TITLE, DESCRIPTION, PAYMENT, NUMBER_OF_WORKERS, DATE_TIME, LOCATION, CREATOR_ID, PAYMENT_TYPE)
        val taskPaginatedResponse = TaskPaginatedResponse(listOf(taskResponse, taskResponse))

        //User
        private const val FIRST_NAME = "Radoslav"
        private const val LAST_NAME = "Hubenov"
        private const val EMAIL = "rrhubenov@gmail.com"
        private const val PASSWORD = "password"
        private val DATE_OF_BIRTH = DateOfBirth(1, 1, 2000)
        val user = User(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, DATE_OF_BIRTH.toString())
        val userRegisterBody = UserRegisterBody(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, DATE_OF_BIRTH)
        val userResponse = UserResponse(user.id, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH)
        val userLoginBody = UserLoginBody(EMAIL, PASSWORD)

        //Application
        private const val ACCEPTED = false
        val application = Application(user, task, ACCEPTED)
        val applicationResponse = ApplicationResponse(application.id, application.user.id, application.task.id, application.accepted)
        val applicationBody = ApplicationBody(task.id)
    }
}