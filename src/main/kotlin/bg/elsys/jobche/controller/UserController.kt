package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.body.user.UserLoginBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.response.UserResponse
import bg.elsys.jobche.service.UserService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.Authorization
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(val userService: UserService) {

    @PostMapping("/login")
    fun login(@RequestBody userLogin: UserLoginBody): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.login(userLogin), HttpStatus.OK)
    }

    @PostMapping
    fun create(@RequestBody userRegister: UserRegisterBody): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.create(userRegister), HttpStatus.CREATED)
    }

    @DeleteMapping
    @ApiOperation(value = "Delete currently signed in user",
            httpMethod = "DELETE",
            authorizations = arrayOf(Authorization(value="basicAuth")))
    fun delete(): ResponseEntity<Unit> {
        return ResponseEntity(userService.delete(), HttpStatus.NO_CONTENT)
    }

    @PutMapping
    fun update(@RequestBody updatedUser: UserRegisterBody): ResponseEntity<Unit> {
        return ResponseEntity(userService.update(updatedUser), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Long): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.read(id), HttpStatus.OK)
    }

}
