package bg.elsys.jobche

import bg.elsys.jobche.entity.body.WorkBody
import bg.elsys.jobche.entity.body.application.ApplicationBody
import bg.elsys.jobche.entity.body.task.Address
import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.entity.body.user.UserLoginBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.model.task.Application
import bg.elsys.jobche.entity.model.task.Task
import bg.elsys.jobche.entity.model.user.User
import bg.elsys.jobche.entity.model.work.Work
import bg.elsys.jobche.entity.model.work.Participation
import bg.elsys.jobche.entity.response.WorkResponse
import bg.elsys.jobche.entity.response.application.ApplicationResponse
import bg.elsys.jobche.entity.response.task.TaskPaginatedResponse
import bg.elsys.jobche.entity.response.task.TaskResponse
import bg.elsys.jobche.entity.response.user.UserResponse
import java.time.LocalDateTime

class DefaultValues {
    companion object {
        //User - Creator
        private const val CREATOR_FIRST_NAME = "Radoslav"
        private const val CREATOR_LAST_NAME = "Hubenov"
        private const val CREATOR_EMAIL = "rrhubenov@gmail.com"
        private const val CREATOR_PASSWORD = "password"
        private val CREATOR_DATE_OF_BIRTH = DateOfBirth(1, 1, 2000)
        private const val CREATOR_PHONE_NUM = "0878555373"
        val creatorUser = User(CREATOR_FIRST_NAME, CREATOR_LAST_NAME, CREATOR_EMAIL, CREATOR_PASSWORD, CREATOR_DATE_OF_BIRTH.toString(), CREATOR_PHONE_NUM)
        val creatorUserRegisterBody = UserRegisterBody(CREATOR_FIRST_NAME, CREATOR_LAST_NAME, CREATOR_EMAIL, CREATOR_PASSWORD, CREATOR_DATE_OF_BIRTH, CREATOR_PHONE_NUM)
        val creatorUserResponse = UserResponse(creatorUser.id, CREATOR_FIRST_NAME, CREATOR_LAST_NAME, CREATOR_DATE_OF_BIRTH, CREATOR_PHONE_NUM)
        val creatorUserLoginBody = UserLoginBody(CREATOR_EMAIL, CREATOR_PASSWORD)

        //User - Worker
        private const val WORKER_FIRST_NAME = "Worker"
        private const val WORKER_LAST_NAME = "Worker"
        private const val WORKER_EMAIL = "worker@gworker.com"
        private const val WORKER_PASSWORD = "worker123"
        private val WORKER_DATE_OF_BIRTH = DateOfBirth(2, 2, 2000)
        private const val WORKER_PHONE_NUM = "0878666872"
        val workerUser = User(WORKER_FIRST_NAME, WORKER_LAST_NAME, WORKER_EMAIL, WORKER_PASSWORD, WORKER_DATE_OF_BIRTH.toString(), WORKER_PHONE_NUM)
        val workerUserRegisterBody = UserRegisterBody(WORKER_FIRST_NAME, WORKER_LAST_NAME, WORKER_EMAIL, WORKER_PASSWORD, WORKER_DATE_OF_BIRTH, WORKER_PHONE_NUM)
        val workerUserResponse = UserResponse(workerUser.id, WORKER_FIRST_NAME, WORKER_LAST_NAME, WORKER_DATE_OF_BIRTH, WORKER_PHONE_NUM)
        val workerUserLoginBody = UserLoginBody(WORKER_EMAIL, WORKER_PASSWORD)

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
        private const val ACCEPTED_COUNT = 0
        val task = Task(TITLE, DESCRIPTION, PAYMENT, NUMBER_OF_WORKERS, DATE_TIME, creatorUser, LOCATION)
        val taskBody = TaskBody(TITLE, PAYMENT, NUMBER_OF_WORKERS, DESCRIPTION, DATE_TIME, LOCATION)
        val taskResponse = TaskResponse(task.id, TITLE, DESCRIPTION, PAYMENT, NUMBER_OF_WORKERS, DATE_TIME, LOCATION, CREATOR_ID, ACCEPTED_COUNT)
        val taskPaginatedResponse = TaskPaginatedResponse(listOf(taskResponse, taskResponse))


        //Application
        private const val ACCEPTED = false
        val application = Application(workerUser, task, ACCEPTED)
        val applicationResponse = ApplicationResponse(application.id, workerUserResponse, taskResponse, application.accepted)
        val applicationBody = ApplicationBody(task.id)

        //Work
        val workBody = WorkBody(task.id, listOf(workerUser.id))
        val work = Work(task)
        val workResponse = WorkResponse(work.id, task, listOf(workerUser), work.createdAt, work.status)

        //Participation
        val participation = Participation(work, workerUser)
    }
}