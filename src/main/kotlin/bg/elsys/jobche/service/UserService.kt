package bg.elsys.jobche.service

import bg.elsys.jobche.entity.body.UserLoginBody
import bg.elsys.jobche.entity.body.UserRegisterBody
import bg.elsys.jobche.entity.model.User
import bg.elsys.jobche.entity.response.UserResponse
import bg.elsys.jobche.exceptions.UserNotFoundException
import bg.elsys.jobche.repositories.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository, val passwordEncoder: PasswordEncoder, val authenticationManager: AuthenticationManager) {
    fun login(userLogin: UserLoginBody): Authentication {
        val authentication = authenticationManager
                .authenticate(UsernamePasswordAuthenticationToken(userLogin.email, userLogin.password))
        return authentication
    }

    fun register(userRegister: UserRegisterBody): UserResponse {
        val userDTO = User(userRegister.firstName,
                userRegister.lastName,
                userRegister.email,
                passwordEncoder.encode(userRegister.password))

        val savedUser = userRepository.save(userDTO)
        return UserResponse(savedUser.id, savedUser.firstName, savedUser.lastName)
    }

}
