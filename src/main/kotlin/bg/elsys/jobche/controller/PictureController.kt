package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.body.picture.ProfilePictureBody
import bg.elsys.jobche.entity.response.picture.PictureResponse
import bg.elsys.jobche.service.PictureService
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Api(value = "Picture operations", description = "Adding pictures to S3 bucket for profile pictures and work pictures")
@RestController
@RequestMapping("/picture")
class PictureController(val pictureService: PictureService) {
    @PostMapping("/profile")
    @ApiOperation(value = "Add profile picture to S3 and database",
            response = PictureResponse::class,
            httpMethod = "POST",
            authorizations = arrayOf(Authorization(value = "basicAuth")))
    @ApiResponses(ApiResponse(code = 201, message = "Created", response = PictureResponse::class))
    fun addProfilePicture(@RequestBody profilePictureBody: ProfilePictureBody, @RequestParam picture: MultipartFile): ResponseEntity<PictureResponse> {
        return ResponseEntity(pictureService.addProfilePicture(profilePictureBody, picture), HttpStatus.CREATED)
    }
}
