package bg.elsys.jobche.entity.response

import bg.elsys.jobche.entity.model.work.WorkStatus
import bg.elsys.jobche.entity.response.task.TaskResponse
import bg.elsys.jobche.entity.response.user.UserResponse
import java.util.*

data class WorkResponse(val id: Long?,
                        val task: TaskResponse?,
                        val workers: List<UserResponse>?,
                        val createdAt: Date?,
                        val status: WorkStatus?)
