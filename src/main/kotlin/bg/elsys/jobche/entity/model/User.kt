package bg.elsys.jobche.entity.model

import bg.elsys.jobche.entity.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table


@Entity
@Table(name = "users")
data class User(var firstName: String,
                var lastName: String,
                var email: String,
                var password: String,
                var dateOfBirth: String,
                var phoneNum: String) : BaseEntity()
