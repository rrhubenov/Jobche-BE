package bg.elsys.jobche.config.security

import bg.elsys.jobche.exceptions.UserNotFoundException
import bg.elsys.jobche.repositories.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component


@Component
class PostgreUserDetailsService(val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)

        if (user == null) {
            throw UserNotFoundException()
        }

        return User(user.email, user.password, listOf(SimpleGrantedAuthority("USER")))
    }
}