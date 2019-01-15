package bg.elsys.jobche.entity.model

import bg.elsys.jobche.entity.BaseEntity
import bg.elsys.jobche.entity.body.task.Address
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tasks")
data class Task(var title: String = "",
                var description: String = "",
                var payment: Int = 0,
                var numberOfWorkers: Int = 0,
                var dateTime: LocalDateTime = LocalDateTime.now(),
                var creatorId: Long = 0,
                @Embedded
                val location: Address = Address("","","")) : BaseEntity()