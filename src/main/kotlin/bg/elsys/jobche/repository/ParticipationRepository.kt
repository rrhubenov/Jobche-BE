package bg.elsys.jobche.repository

import bg.elsys.jobche.entity.model.work.Participation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParticipationRepository: JpaRepository<Participation, Long>
