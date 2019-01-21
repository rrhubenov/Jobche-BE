package bg.elsys.jobche.entity.model

import bg.elsys.jobche.entity.BaseEntity
import bg.elsys.jobche.entity.body.task.Address
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tasks")
data class Task(var title: String,
                var description: String,
                var payment: Int,
                var numberOfWorkers: Int,
                var dateTime: LocalDateTime,
                var creatorId: Long,
                @Embedded
                val location: Address) : BaseEntity()