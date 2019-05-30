package bg.elsys.jobche.config.security

import bg.elsys.jobche.entity.model.user.User
import bg.elsys.jobche.exception.UserNotFoundException
import bg.elsys.jobche.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthenticationDetails(val userRepository: UserRepository) {
    fun getEmail(): String {
        return SecurityContextHolder.getContext().authentication.name
    }

    fun getId(): Long {
        val user = userRepository.findByEmail(getEmail())
        if (user != null) {
            return user.id
        } else throw UserNotFoundException()
    }

    fun getUser(): User {
        val user = userRepository.findByEmail(getEmail())
        if (user != null) {
            return user
        } else throw UserNotFoundException()
    }
}