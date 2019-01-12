package bg.elsys.jobche.config.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthenticationDetails {
    fun getEmail(): String {
        return SecurityContextHolder.getContext().authentication.name
    }
}