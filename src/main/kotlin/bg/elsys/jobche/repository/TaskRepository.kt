package bg.elsys.jobche.repository

import bg.elsys.jobche.entity.model.task.Task
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long>{
    fun findAllByCreatorId(pageable: Pageable, creatorId: Long?): Page<Task>
}
