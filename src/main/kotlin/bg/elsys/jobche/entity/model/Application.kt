package bg.elsys.jobche.entity.model

import bg.elsys.jobche.entity.BaseEntity
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "applications", uniqueConstraints = [ UniqueConstraint(columnNames = ["user_id", "task_id"]) ])
data class Application(@ManyToOne(optional = false)
                       val user: User,
                       @ManyToOne(optional = false)
                       val task: Task,
                       var accepted: Boolean = false) : BaseEntity()
