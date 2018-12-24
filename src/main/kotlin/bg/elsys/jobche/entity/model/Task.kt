package bg.elsys.jobche.entity.model

import bg.elsys.jobche.entity.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "Tasks")
data class Task(val title: String = "",
                val description: String = "",
                val payment: Int = 0,
                val numberOfWorkers: Int = 0,
                val dateTime: LocalDateTime = LocalDateTime.now()) : BaseEntity()