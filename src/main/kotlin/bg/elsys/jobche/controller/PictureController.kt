package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.body.picture.ProfilePictureBody
import bg.elsys.jobche.entity.response.TaskPicturesResponse
import bg.elsys.jobche.entity.response.picture.PictureResponse
import bg.elsys.jobche.service.PictureService
import io.swagger.annotations.*
import org.apache.commons.io.IOUtils
import org.apache.http.entity.ContentType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

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
    fun addProfilePicture(@RequestParam("file") picture: MultipartFile): ResponseEntity<PictureResponse> {
        return ResponseEntity(pictureService.addProfilePicture(picture), HttpStatus.CREATED)
    }

    @GetMapping("/profile")
    @ApiOperation(value = "Get the profile picture of the current user",
            httpMethod =  "GET",
            authorizations = arrayOf(Authorization(value = "basicAuth")))
    @ApiResponses(ApiResponse(code = 200, message = "OK"))
    fun getProfilePicture(response: HttpServletResponse) {
        val stream = pictureService.getProfilePicture(null)
        val bufferSize = 1024*1024
        response.bufferSize = bufferSize
        response.contentType = ContentType.APPLICATION_OCTET_STREAM.toString()
        val length = IOUtils.copy(stream, response.outputStream)
        response.setContentLength(length)
        response.flushBuffer()
    }

    @PostMapping("/task/{id}")
    @ApiOperation(value = "Add picture for task",
            response = PictureResponse::class,
            httpMethod = "POST",
            authorizations = arrayOf(Authorization(value = "basicAuth")))
    @ApiResponses(ApiResponse(code = 201, message = "Created", response = PictureResponse::class))
    fun addTaskPicture(@RequestParam("file") picture: MultipartFile, @PathVariable("id") id: Long): ResponseEntity<PictureResponse> {
        return ResponseEntity(pictureService.addTaskPicture(picture, id), HttpStatus.CREATED)
    }

    @GetMapping("/task/{id}")
    @ApiOperation(value = "Return a json with hyperlink for all the images for a task",
            response = TaskPicturesResponse::class,
            httpMethod = "GET",
            authorizations = arrayOf(Authorization(value = "basicAuth")))
    @ApiResponses(ApiResponse(code = 200, message = "OK", response = TaskPicturesResponse::class))
    fun getPicturesForTask(@PathVariable("id") id: Long): ResponseEntity<TaskPicturesResponse> {
        return ResponseEntity(pictureService.getPicturesForTask(id), HttpStatus.OK)
    }

}
