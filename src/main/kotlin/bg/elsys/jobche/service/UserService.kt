package bg.elsys.jobche.service

import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.entity.body.user.UserLoginBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.model.User
import bg.elsys.jobche.entity.response.user.UserResponse
import bg.elsys.jobche.exceptions.UserNotFoundException
import bg.elsys.jobche.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository,
                  val passwordEncoder: PasswordEncoder,
                  val authenticationDetails: AuthenticationDetails) {

    fun login(userLogin: UserLoginBody): UserResponse {
        if (userRepository.existsByEmail(userLogin.email)) {
            val user = userRepository.findByEmail(userLogin.email)
            return UserResponse(user?.id, user?.firstName, user?.lastName)
        } else throw UserNotFoundException()
    }

    fun create(userRegister: UserRegisterBody): UserResponse {
        val dateOfBirth = userRegister.dateOfBirth

        val userDTO = User(userRegister.firstName,
                userRegister.lastName,
                userRegister.email,
                passwordEncoder.encode(userRegister.password),
                dateOfBirth.toString())

        val savedUser = userRepository.save(userDTO)
        return UserResponse(savedUser.id, savedUser.firstName, savedUser.lastName)
    }

    fun delete() {
        userRepository.deleteByEmail(authenticationDetails.getEmail())
    }

    fun update(updatedUser: UserRegisterBody) {
        val user = userRepository.getOneByEmail(authenticationDetails.getEmail())
        user.firstName = updatedUser.firstName
        user.lastName = updatedUser.lastName
        user.email = updatedUser.email
        user.password = passwordEncoder.encode(updatedUser.password)
        userRepository.save(user)
    }

    fun read(id: Long): UserResponse {
        if( userRepository.existsById(id) ) {
            val user = userRepository.findById(id).get()
            return UserResponse(user.id, user.firstName, user.lastName)
        } else throw UserNotFoundException()
    }

}
