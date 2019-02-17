package bg.elsys.jobche.repository

import bg.elsys.jobche.entity.model.task.Application
import bg.elsys.jobche.entity.model.user.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationRepository : JpaRepository<Application, Long> {
    fun findAllByUser(pageable: Pageable, user: User?): Page<Application>
}
