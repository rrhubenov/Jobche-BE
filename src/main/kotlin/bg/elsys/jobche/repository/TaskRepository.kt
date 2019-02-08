package bg.elsys.jobche.repository

import bg.elsys.jobche.entity.model.task.Task
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.criteria.Predicate

@Repository
interface TaskRepository : JpaRepository<Task, Long>, CustomTaskRepository {
    fun findAllByCreatorId(pageable: Pageable, creatorId: Long?): Page<Task>
}

interface CustomTaskRepository {
    fun findAll(pageable: Pageable,
                title: String?,
                paymentStart: Int?,
                paymentEnd: Int?,
                numWStart: Int?,
                numWEnd: Int?,
                dateStart: LocalDateTime?,
                city: String?): List<Task>
}

@Repository
class CustomTaskRepositoryImpl(val em: EntityManager) : CustomTaskRepository {
    override fun findAll(pageable: Pageable, title: String?, paymentStart: Int?, paymentEnd: Int?, numWStart: Int?, numWEnd: Int?, dateStart: LocalDateTime?, city: String?): List<Task> {
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(Task::class.java)

        val task = cq.from(Task::class.java)
        val predicates = mutableListOf<Predicate>()

        if (title != null) {
            predicates.add(cb.like(task.get("title"), "%" + title + "%"))
        }

        if (paymentStart != null) {
            predicates.add(cb.greaterThanOrEqualTo(task.get("payment"), paymentStart))
        }

        if (paymentEnd != null) {
            predicates.add(cb.lessThanOrEqualTo(task.get("payment"), paymentEnd))
        }

        if (numWStart != null) {
            predicates.add(cb.greaterThanOrEqualTo(task.get("numberOfWorkers"), numWStart))
        }

        if (numWEnd != null) {
            predicates.add(cb.lessThanOrEqualTo(task.get("numberOfWorkers"), numWEnd))
        }

        if (city != null) {
            predicates.add(cb.equal(task.get<String>("location").get<String>("city"), city))
        }

            cq.where(*predicates.toTypedArray())

        return em.createQuery(cq).resultList
    }
}
