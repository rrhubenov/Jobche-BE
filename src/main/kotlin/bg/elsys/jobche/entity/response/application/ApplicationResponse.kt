package bg.elsys.jobche.entity.response.application

import bg.elsys.jobche.entity.response.task.TaskResponse
import bg.elsys.jobche.entity.response.user.UserResponse

data class ApplicationResponse(val id: Long?,
                               val applicant: UserResponse?,
                               val task: TaskResponse?,
                               var accepted: Boolean)
