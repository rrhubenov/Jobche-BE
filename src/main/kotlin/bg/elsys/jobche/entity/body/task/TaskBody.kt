package bg.elsys.jobche.entity.body.task

import java.time.LocalDateTime

data class TaskBody(val title: String,
                    val payment: Int,
                    val numberOfWorkers: Int,
                    val description: String,
                    val dateTime: LocalDateTime,
                    val city: String)
