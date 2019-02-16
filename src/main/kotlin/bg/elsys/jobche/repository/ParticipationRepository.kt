package bg.elsys.jobche.repository

import bg.elsys.jobche.entity.model.work.Participation
import org.springframework.data.jpa.repository.JpaRepository

interface ParticipationRepository: JpaRepository<Participation, Long>
