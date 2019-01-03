package bg.elsys.jobche.entity.model

import bg.elsys.jobche.entity.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "Tasks")
data class Task(var title: String = "",
                var description: String = "",
                var payment: Int = 0,
                var numberOfWorkers: Int = 0,
                var dateTime: LocalDateTime = LocalDateTime.now()) : BaseEntity()