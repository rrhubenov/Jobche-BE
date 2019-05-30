package bg.elsys.jobche.repository

import bg.elsys.jobche.entity.model.picture.TaskPicture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskPictureRepository: JpaRepository<TaskPicture, Long>
