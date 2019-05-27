package bg.elsys.jobche

import bg.elsys.jobche.entity.body.WorkBody
import bg.elsys.jobche.entity.body.application.ApplicationBody
import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.entity.body.user.UserLoginBody
import bg.elsys.jobche.entity.body.user.UserBody
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

        //User - Worker
        private const val WORKER_FIRST_NAME = "Worker"
        private const val WORKER_LAST_NAME = "Worker"
        private const val WORKER_EMAIL = "worker@gworker.com"
        private const val WORKER_PASSWORD = "worker123"
        private val WORKER_DATE_OF_BIRTH = DateOfBirth(2, 2, 2000)
        private const val WORKER_PHONE_NUM = "0878666872"

        //Task
        private const val TITLE = "Test Title"
        private const val PAYMENT = 10
        private const val NUMBER_OF_WORKERS = 1
        private const val DESCRIPTION = "Test Description"
        private const val COUNTRY = "Bulgaria"
        private const val CITY = "Sofia"
        private val DATE_TIME = LocalDateTime.now()
        private const val CREATOR_ID = 1L
        private const val ACCEPTED_COUNT = 0

        //Application
        private const val ACCEPTED = false

        fun creatorUser(): User {
            return User(CREATOR_FIRST_NAME, CREATOR_LAST_NAME, CREATOR_EMAIL, CREATOR_PASSWORD, CREATOR_DATE_OF_BIRTH.toString(), CREATOR_PHONE_NUM)
        }

        fun creatorUserBody(): UserBody {
            return UserBody(CREATOR_FIRST_NAME, CREATOR_LAST_NAME, CREATOR_EMAIL, CREATOR_PASSWORD, CREATOR_DATE_OF_BIRTH, CREATOR_PHONE_NUM)
        }

        fun creatorUserResponse(): UserResponse {
            val creatorUser = creatorUser()
            return UserResponse(creatorUser.id, CREATOR_FIRST_NAME, CREATOR_LAST_NAME, CREATOR_DATE_OF_BIRTH, CREATOR_PHONE_NUM)
        }

        fun workerUser(): User {
            return User(WORKER_FIRST_NAME, WORKER_LAST_NAME, WORKER_EMAIL, WORKER_PASSWORD, WORKER_DATE_OF_BIRTH.toString(), WORKER_PHONE_NUM)
        }

        fun workerUserBody(): UserBody {
            return UserBody(WORKER_FIRST_NAME, WORKER_LAST_NAME, WORKER_EMAIL, WORKER_PASSWORD, WORKER_DATE_OF_BIRTH, WORKER_PHONE_NUM)
        }

        fun workerUserResponse(): UserResponse {
            val workerUser = workerUser()
            return UserResponse(workerUser.id, WORKER_FIRST_NAME, WORKER_LAST_NAME, WORKER_DATE_OF_BIRTH, WORKER_PHONE_NUM)
        }

        fun task(): Task {
            val creatorUser = creatorUser()
            return Task(TITLE, DESCRIPTION, PAYMENT, NUMBER_OF_WORKERS, DATE_TIME, creatorUser, CITY)
        }

        fun taskBody(): TaskBody {
            return TaskBody(TITLE, PAYMENT, NUMBER_OF_WORKERS, DESCRIPTION, DATE_TIME, CITY)
        }

        fun taskResponse(): TaskResponse {
            val task = task()
            return TaskResponse(task.id, TITLE, DESCRIPTION, PAYMENT, NUMBER_OF_WORKERS, DATE_TIME, CITY, CREATOR_ID, ACCEPTED_COUNT)
        }

        fun taskPaginatedResponse(): TaskPaginatedResponse {
            val taskResponse = taskResponse()
            return TaskPaginatedResponse(listOf(taskResponse, taskResponse))
        }

        fun application(): Application {
            val workerUser = workerUser()
            val task = task()
            return Application(workerUser, task, ACCEPTED)
        }

        fun applicationResponse(): ApplicationResponse {
            val workerUserResponse = workerUserResponse()
            val taskResponse = taskResponse()
            val application = application()
            return ApplicationResponse(application.id, workerUserResponse, taskResponse, application.accepted)
        }

        fun applicationBody(): ApplicationBody {
            val task = task()
            return ApplicationBody(task.id)
        }

        fun workBody(): WorkBody {
            val task = task()
            val workerUser = workerUser()
            return WorkBody(task.id, listOf(workerUser.id))
        }

        fun work(): Work {
            val task = task()
            return Work(task)
        }

        fun workResponse(): WorkResponse {
            val work = work()
            val taskResponse = taskResponse()
            val workerUserResponse = workerUserResponse()
            return WorkResponse(work.id, taskResponse, listOf(workerUserResponse), work.createdAt, work.status)
        }

        fun participation(): Participation {
            val work = work()
            val workerUser = workerUser()
            return Participation(work, workerUser)
        }
    }
}