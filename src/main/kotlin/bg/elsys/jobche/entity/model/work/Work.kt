package bg.elsys.jobche.entity.model.work

import bg.elsys.jobche.entity.BaseEntity
import bg.elsys.jobche.entity.model.task.Task
import bg.elsys.jobche.entity.model.work.Participation
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "works")
@JsonIgnoreProperties( "participations")
data class Work(
        @OneToOne(fetch = FetchType.EAGER)
        val task: Task,
        @OneToMany(mappedBy = "work", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        var participations: List<Participation> = emptyList(),
        var inProgress: Boolean = true) : BaseEntity() {
        override fun toString(): String {
                return ""
        }
}
