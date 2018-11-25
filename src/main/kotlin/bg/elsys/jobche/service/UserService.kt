package bg.elsys.jobche.service

import bg.elsys.jobche.entity.User
import bg.elsys.jobche.entity.response.UserResponse
import bg.elsys.jobche.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) {
    fun getUser(id: Long): UserResponse {
        val user = userRepository.findById(id).get()
        return UserResponse(user.id, user.firstName, user.lastName)
    }

    fun addUser(user: User): Long {
        userRepository.save(user)
        return user.id
    }

}
