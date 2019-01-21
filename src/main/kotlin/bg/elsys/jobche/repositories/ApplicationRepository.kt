package bg.elsys.jobche.repositories

import bg.elsys.jobche.entity.model.Application
import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationRepository: JpaRepository<Application, Long>
