package bg.elsys.jobche.service

import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.converter.Converters
import bg.elsys.jobche.entity.body.user.UserLoginBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.model.user.User
import bg.elsys.jobche.entity.response.user.UserResponse
import bg.elsys.jobche.exception.EmailExistsException
import bg.elsys.jobche.exception.UserNotFoundException
import bg.elsys.jobche.exceptions.PhoneNumberExistsException
import bg.elsys.jobche.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository,
                  val passwordEncoder: PasswordEncoder,
                  val authenticationDetails: AuthenticationDetails,
                  val converters: Converters = Converters()) {

    fun login(userLogin: UserLoginBody): UserResponse {
        if (userRepository.existsByEmail(userLogin.email)) {
            val user = userRepository.findByEmail(userLogin.email)
            with(converters) {
                return user!!.response
            }
        } else throw UserNotFoundException()
    }

    fun create(userRegister: UserRegisterBody): UserResponse {
        if (userRepository.existsByEmail(userRegister.email)) {
            throw EmailExistsException()
        }

        if (userRepository.existsByPhoneNum(userRegister.phoneNum)) {
            throw PhoneNumberExistsException()
        }

        val dateOfBirth = userRegister.dateOfBirth

        val userDTO = User(userRegister.firstName,
                userRegister.lastName,
                userRegister.email,
                passwordEncoder.encode(userRegister.password),
                dateOfBirth.toString(),
                userRegister.phoneNum
        )

        val savedUser = userRepository.save(userDTO)
        with(converters) {
            return savedUser.response
        }
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
        user.dateOfBirth = updatedUser.dateOfBirth.toString()
        user.phoneNum = updatedUser.phoneNum
        userRepository.save(user)
    }

    fun read(id: Long): UserResponse {
        if (userRepository.existsById(id)) {
            val user = userRepository.findById(id).get()
            with(converters) {
                return user.response
            }
        } else throw UserNotFoundException()
    }

}
