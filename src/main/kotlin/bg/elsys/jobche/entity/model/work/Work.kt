package bg.elsys.jobche.entity.model.work

import bg.elsys.jobche.entity.BaseEntity
import bg.elsys.jobche.entity.model.task.Task
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "works")
@JsonIgnoreProperties("participations")
data class Work(

        @OneToOne(fetch = FetchType.EAGER)
        val task: Task,

        @OneToMany(mappedBy = "work", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
        var participations: List<Participation> = emptyList(),

        var status: WorkStatus = WorkStatus.IN_PROGRESS) : BaseEntity() {

    override fun toString(): String {
        return ""
    }
}

enum class WorkStatus {
    IN_PROGRESS, ENDED
}
