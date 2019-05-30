package bg.elsys.jobche.entity.model.picture

import bg.elsys.jobche.entity.model.user.User
import javax.persistence.*

@Entity
@Table(name = "profilePictures")
data class ProfilePicture(@OneToOne(fetch = FetchType.EAGER)
                          val user: User) : Picture()