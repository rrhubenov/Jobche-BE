package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.body.user.UserLoginBody
import bg.elsys.jobche.entity.body.user.UserRegisterBody
import bg.elsys.jobche.entity.response.UserResponse
import bg.elsys.jobche.service.UserService
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(value = "User Operations", description = "All operations for users")
@RestController
@RequestMapping("/users")
class UserController(val userService: UserService) {

    @PostMapping("/login")
    @ApiOperation(value = "Get information about user",
            httpMethod = "POST")
    @ApiResponses(ApiResponse(code = 204, message = "No content", response = UserResponse::class))
    fun login(@RequestBody userLogin: UserLoginBody): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.login(userLogin), HttpStatus.OK)
    }

    @PostMapping
    @ApiOperation(value = "Create a new user",
            httpMethod = "POST")
    @ApiResponses(ApiResponse(code = 201, message = "Created", response = UserResponse::class))
    fun create(@RequestBody userRegister: UserRegisterBody): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.create(userRegister), HttpStatus.CREATED)
    }

    @DeleteMapping
    @ApiOperation(value = "Delete currently signed in user",
            httpMethod = "DELETE",
            authorizations = arrayOf(Authorization(value="basicAuth")))
    @ApiResponses(ApiResponse(code = 204, message = "No content", response = Unit::class))
    fun delete(): ResponseEntity<Unit> {
        return ResponseEntity(userService.delete(), HttpStatus.NO_CONTENT)
    }

    @PutMapping
    @ApiOperation(value = "Update currently signed in user",
            httpMethod = "PUT",
            authorizations = arrayOf(Authorization(value="basicAuth")))
    @ApiResponses(ApiResponse(code = 200, message = "Success", response = Unit::class))
    fun update(@RequestBody updatedUser: UserRegisterBody): ResponseEntity<Unit> {
        return ResponseEntity(userService.update(updatedUser), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Read info of user",
            httpMethod = "GET",
            authorizations = arrayOf(Authorization(value="basicAuth")))
    @ApiResponses(ApiResponse(code = 200, message = "Success", response = UserResponse::class))
    fun read(@PathVariable id: Long): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.read(id), HttpStatus.OK)
    }

}
