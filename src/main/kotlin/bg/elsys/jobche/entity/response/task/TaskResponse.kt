package bg.elsys.jobche.entity.response.task

import bg.elsys.jobche.entity.response.user.UserResponse
import java.time.LocalDateTime

data class TaskResponse(val id: Long?,
                        val title: String?,
                        val description: String?,
                        val payment: Int?,
                        val numberOfWorkers: Int?,
                        val dateTime: LocalDateTime?,
                        val city: String?,
                        val creator: UserResponse?,
                        val acceptedWorkersCount: Int?,
                        val pictures: List<String>? = null)
