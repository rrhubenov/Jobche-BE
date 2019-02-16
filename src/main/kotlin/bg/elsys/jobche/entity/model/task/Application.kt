package bg.elsys.jobche.entity.model.task

import bg.elsys.jobche.entity.BaseEntity
import bg.elsys.jobche.entity.model.user.User
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "applications", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "task_id"])])
data class Application(@ManyToOne(optional = false, fetch = FetchType.EAGER)
                       val user: User,
                       @ManyToOne(optional = false, fetch = FetchType.EAGER)
                       val task: Task,
                       var accepted: Boolean = false) : BaseEntity() {
    override fun toString(): String {
        return ""
    }
}
