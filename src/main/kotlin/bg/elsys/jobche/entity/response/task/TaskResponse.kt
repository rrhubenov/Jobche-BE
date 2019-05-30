package bg.elsys.jobche.entity.response.task

import java.time.LocalDateTime

data class TaskResponse(val id: Long?,
                        val title: String?,
                        val description: String?,
                        val payment: Int?,
                        val numberOfWorkers: Int?,
                        val dateTime: LocalDateTime?,
                        val city: String?,
                        val creatorId: Long?,
                        val acceptedWorkersCount: Int?,
                        val pictures: List<String>? = null)
