package bg.elsys.jobche.repository

import bg.elsys.jobche.entity.model.picture.ProfilePicture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfilePictureRepository : JpaRepository<ProfilePicture, Long>
