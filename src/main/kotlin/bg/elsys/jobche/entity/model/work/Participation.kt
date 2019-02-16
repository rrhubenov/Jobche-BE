package bg.elsys.jobche.entity.model.work

import bg.elsys.jobche.entity.BaseEntity
import bg.elsys.jobche.entity.model.user.User
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "participations")
data class Participation(
        @ManyToOne(fetch = FetchType.EAGER)
        val work: Work,
        @ManyToOne(fetch = FetchType.EAGER)
        val user: User) : BaseEntity() {
        override fun toString(): String {
                return ""
        }

}
