package bg.elsys.jobche.repository

import bg.elsys.jobche.entity.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    @Transactional
    fun deleteByEmail(email: String)

    @Transactional
    fun getOneByEmail(email: String): User

    fun existsByPhoneNum(phoneNum: String): Boolean
}