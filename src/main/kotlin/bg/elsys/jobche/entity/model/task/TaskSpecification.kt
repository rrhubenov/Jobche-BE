package bg.elsys.jobche.entity.model.task

import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class TaskSpecification(criteria: Task): Specification<Task> {
    override fun toPredicate(root: Root<Task>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder): Predicate? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}