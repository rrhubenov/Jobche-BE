package bg.elsys.jobche.converter

import bg.elsys.jobche.entity.body.user.DateOfBirth
import bg.elsys.jobche.entity.model.Application
import bg.elsys.jobche.entity.model.User
import bg.elsys.jobche.entity.model.task.Task
import bg.elsys.jobche.entity.response.application.ApplicationResponse
import bg.elsys.jobche.entity.response.task.TaskResponse
import bg.elsys.jobche.entity.response.user.UserResponse
import org.springframework.stereotype.Component

@Component
class Converters {
    val Task.response
        get() = TaskResponse(id, title, description, payment, numberOfWorkers, dateTime, location, creatorId)

    val User.response
        get() = UserResponse(id, firstName, lastName, toDateOfBirth(dateOfBirth))

    val Application.response
        get() = ApplicationResponse(id, user.response, task.response, accepted)

    fun toDateOfBirth(date: String): DateOfBirth {
        val values = date.split("-")
        return DateOfBirth(values.get(0).toInt(), values.get(1).toInt(), values.get(2).toInt())
    }
}