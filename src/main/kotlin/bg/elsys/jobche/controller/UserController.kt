package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.body.UserLoginBody
import bg.elsys.jobche.entity.body.UserRegisterBody
import bg.elsys.jobche.entity.response.UserResponse
import bg.elsys.jobche.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
//@RequestMapping("/user")
class UserController(val userService: UserService) {

    @PostMapping("/user/login")
    fun login(@RequestBody userLogin : UserLoginBody): ResponseEntity<Authentication> {
        return ResponseEntity(userService.login(userLogin), HttpStatus.OK)
    }

    @PostMapping("/user/register")
    fun register(@RequestBody userRegister: UserRegisterBody): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.register(userRegister), HttpStatus.CREATED)
    }

    @GetMapping("/private")
    fun private() : String{
        return "Private nigga"
    }

}
