package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.User
import bg.elsys.jobche.entity.response.UserResponse
import bg.elsys.jobche.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(val userService: UserService) {

    @GetMapping("/{id}")
    fun getUser(@PathVariable("id") id: Long): UserResponse {
        return userService.getUser(id)
    }

    @PostMapping
    fun register(@RequestBody user: User): ResponseEntity<Long> {
        return ResponseEntity<Long>(userService.addUser(user), HttpStatus.CREATED)
    }

}
