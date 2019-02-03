package bg.elsys.jobche.entity.response.task

import bg.elsys.jobche.entity.body.task.Address
import bg.elsys.jobche.entity.model.task.PaymentType
import java.time.LocalDateTime

data class TaskResponse(val id: Long?,
                        val title: String?,
                        val description: String?,
                        val payment: Int?,
                        val numberOfWorkers: Int?,
                        val dateTime: LocalDateTime?,
                        val location: Address?,
                        val creatorId: Long?,
                        val paymentType: PaymentType?)
