package bg.elsys.jobche.entity.model.task

import bg.elsys.jobche.entity.BaseEntity
import bg.elsys.jobche.entity.model.picture.TaskPicture
import bg.elsys.jobche.entity.model.user.User
import bg.elsys.jobche.entity.model.work.Work
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "tasks")
@JsonIgnoreProperties("applications", "work")
data class Task(
        @NotNull
        var title: String,

        @NotNull
        var description: String,

        @NotNull
        var payment: Int,

        @NotNull
        @Size(min = 1, message = "The number of workers needed cannot be less than 1")
        var numberOfWorkers: Int,

        @NotNull
        var dateTime: LocalDateTime,

        @NotNull
        @ManyToOne(fetch = FetchType.EAGER)
        var creator: User,

        @NotNull
        var city: String,

        var acceptedWorkersCount: Int = 0,

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "task", fetch = FetchType.LAZY)
        var applications: List<Application> = emptyList(),

        @OneToOne(fetch = FetchType.LAZY, mappedBy = "task", cascade = [CascadeType.ALL])
        var work: Work? = null,

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "task", fetch = FetchType.LAZY)
        var pictures: List<TaskPicture>? = null
        ) : BaseEntity() {


    override fun toString(): String {
        return ""
    }
}