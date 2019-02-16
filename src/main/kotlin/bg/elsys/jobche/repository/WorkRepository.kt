package bg.elsys.jobche.repository

import bg.elsys.jobche.entity.model.work.Work
import org.springframework.data.jpa.repository.JpaRepository

interface WorkRepository: JpaRepository<Work, Long>
