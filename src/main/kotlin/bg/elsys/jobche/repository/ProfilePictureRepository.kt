package bg.elsys.jobche.repository

import bg.elsys.jobche.entity.model.picture.ProfilePicture
import bg.elsys.jobche.entity.model.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfilePictureRepository : JpaRepository<ProfilePicture, Long> {
    fun existsByUser(user: User): Boolean
    fun findByUser(user: User): ProfilePicture
}
