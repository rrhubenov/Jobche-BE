package bg.elsys.jobche.entity

import javax.persistence.*

@Entity
@Table(name = "Users")
data class User(val firstName: String = "",
                val lastName: String = "",
                val email: String = "") : BaseEntity()
