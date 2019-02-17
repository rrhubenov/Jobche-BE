package bg.elsys.jobche.repository

import bg.elsys.jobche.entity.model.work.Work
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WorkRepository: JpaRepository<Work, Long>
