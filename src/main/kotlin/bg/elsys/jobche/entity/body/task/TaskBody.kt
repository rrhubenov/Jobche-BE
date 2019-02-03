package bg.elsys.jobche.entity.body.task

import bg.elsys.jobche.entity.model.task.PaymentType
import java.time.LocalDateTime

data class TaskBody(val title: String,
                    val payment: Int,
                    val numberOfWorkers: Int,
                    val description: String,
                    val dateTime: LocalDateTime,
                    val location: Address,
                    val paymentType: PaymentType)
