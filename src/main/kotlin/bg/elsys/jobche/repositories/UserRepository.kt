package bg.elsys.jobche.repositories

import bg.elsys.jobche.entity.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(username: String) : User?
    fun existsByEmail(email: String): Boolean
    @Transactional
    fun deleteByEmail(email: String)
    @Transactional
    fun getOneByEmail(email: String): User
}