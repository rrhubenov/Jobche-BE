package bg.elsys.jobche.entity.model.user

import bg.elsys.jobche.entity.BaseEntity
import bg.elsys.jobche.entity.model.task.Application
import bg.elsys.jobche.entity.model.task.Task
import bg.elsys.jobche.entity.model.work.Participation
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*


@Entity
@Table(name = "users")
@JsonIgnoreProperties("tasks", "applications", "participations")
data class User(
        var firstName: String,

        var lastName: String,

        var email: String,

        var password: String,

        var dateOfBirth: String,

        var phoneNum: String,

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY)
        var tasks: List<Task> = emptyList(),

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", fetch = FetchType.LAZY)
        var applications: List<Application> = emptyList(),

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", fetch = FetchType.LAZY)
        var participations: List<Participation> = emptyList()) : BaseEntity() {

    override fun toString(): String {
        return ""
    }
}

