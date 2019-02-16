package bg.elsys.jobche.entity.model.task

import bg.elsys.jobche.entity.BaseEntity
import bg.elsys.jobche.entity.body.task.Address
import bg.elsys.jobche.entity.model.user.User
import bg.elsys.jobche.entity.model.work.Work
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tasks")
@JsonIgnoreProperties("applications", "work")
data class Task(
        var title: String,

        var description: String,

        var payment: Int,

        var numberOfWorkers: Int,

        var dateTime: LocalDateTime,

        @ManyToOne(fetch = FetchType.EAGER)
        var creator: User,

        @Embedded
        var location: Address,

        var acceptedWorkersCount: Int = 0,

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "task", fetch = FetchType.LAZY)
        var applications: List<Application> = emptyList(),

        @OneToOne(fetch = FetchType.LAZY, mappedBy = "task", cascade = [CascadeType.ALL])
        var work: Work? = null) : BaseEntity() {


    override fun toString(): String {
        return ""
    }
}