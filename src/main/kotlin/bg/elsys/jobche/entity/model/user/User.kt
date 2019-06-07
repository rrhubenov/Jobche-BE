package bg.elsys.jobche.entity.model.user

import bg.elsys.jobche.entity.BaseEntity
import bg.elsys.jobche.entity.model.picture.ProfilePicture
import bg.elsys.jobche.entity.model.task.Application
import bg.elsys.jobche.entity.model.task.Task
import bg.elsys.jobche.entity.model.work.Participation
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.jetbrains.annotations.NotNull
import javax.persistence.*


@Entity
@Table(name = "users")
@JsonIgnoreProperties("tasks", "applications", "participations")
data class User(
        @NotNull
        var firstName: String,

        @NotNull
        var lastName: String,

        @NotNull
        var email: String,

        @NotNull
        var password: String,

        @NotNull
        var dateOfBirth: String,

        @NotNull
        var phoneNum: String,

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "creator", fetch = FetchType.LAZY)
        var tasks: List<Task> = emptyList(),

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", fetch = FetchType.LAZY)
        var applications: List<Application> = emptyList(),

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", fetch = FetchType.LAZY)
        var participations: List<Participation> = emptyList(),

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", fetch = FetchType.LAZY)
        var reviews: List<Review> = emptyList(),

        @OneToOne(cascade = [CascadeType.REMOVE], mappedBy = "user", fetch = FetchType.EAGER)
        var picture: ProfilePicture? = null) : BaseEntity() {

    override fun toString(): String {
        return ""
    }
}

