package bg.elsys.jobche.entity.model.user

import bg.elsys.jobche.entity.BaseEntity
import bg.elsys.jobche.entity.model.work.Work
import javax.persistence.*

@Entity
@Table(name = "reviews", uniqueConstraints = [UniqueConstraint(columnNames = ["work_id", "user_id"])])
data class Review(

        @ManyToOne(fetch = FetchType.EAGER)
        val user: User,

        @ManyToOne(fetch = FetchType.EAGER)
        val work: Work,

        @Enumerated(EnumType.STRING)
        val reviewGrade: ReviewGrade,

        val comment: String?

): BaseEntity()


enum class ReviewGrade {
    BAD, AVERAGE, GOOD, GREAT, PERFECT
}