package bg.elsys.jobche.entity.response.task

import bg.elsys.jobche.entity.body.task.Address
import java.time.LocalDateTime

data class TaskResponse(val id: Long?,
                        val title: String?,
                        val description: String?,
                        val payment: Int?,
                        val numberOfWorkers: Int?,
                        val dateTime: LocalDateTime?,
                        val location: Address?,
                        val creatorId: Long?,
                        val acceptedWorkersCount: Int?)
