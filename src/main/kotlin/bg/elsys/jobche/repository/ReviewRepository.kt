package bg.elsys.jobche.repository

import bg.elsys.jobche.entity.model.user.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository: JpaRepository<Review, Long>