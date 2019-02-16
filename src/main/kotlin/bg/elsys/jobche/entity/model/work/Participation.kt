package bg.elsys.jobche.entity.model.work

import bg.elsys.jobche.entity.BaseEntity
import bg.elsys.jobche.entity.model.user.User
import javax.persistence.*

@Entity
@Table(name = "participations", uniqueConstraints = [UniqueConstraint(columnNames = ["work_id", "user_id"])])
data class Participation(
        @ManyToOne(fetch = FetchType.EAGER)
        val work: Work,

        @ManyToOne(fetch = FetchType.EAGER)
        val user: User) : BaseEntity() {

    override fun toString(): String {
        return ""
    }

}
