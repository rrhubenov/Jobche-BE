package bg.elsys.jobche.controller

import bg.elsys.jobche.converter.Converters
import bg.elsys.jobche.entity.body.application.ApplicationBody
import bg.elsys.jobche.entity.response.application.ApplicationResponse
import bg.elsys.jobche.service.ApplicationService
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(value = "Application operations", description = "Operations for creating and approving user applications for tasks")
@RestController
@RequestMapping("/application")
class ApplicationController(val service: ApplicationService, val converters: Converters) {

    @PostMapping
    @ApiOperation(value = "Create application",
            response = ApplicationResponse::class,
            httpMethod = "POST",
            authorizations = arrayOf(Authorization(value = "basicAuth")))
    @ApiResponses(ApiResponse(code = 201, message = "Success", response = ApplicationResponse::class))
    fun create(@RequestBody applicationBody: ApplicationBody): ResponseEntity<ApplicationResponse> {
        val application = service.create(applicationBody)

        with(converters) {
            val applicationResponse = ApplicationResponse(application.id, application.user.response, application.task?.response, application.accepted)
            return ResponseEntity(applicationResponse, HttpStatus.CREATED)
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete application",
            response = ApplicationResponse::class,
            httpMethod = "DELETE",
            authorizations = [Authorization(value = "basicAuth")])
    @ApiResponses(ApiResponse(code = 204, message = "No Content", response = Unit::class))
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity(service.delete(id), HttpStatus.NO_CONTENT)
    }

    @PostMapping("/approve/{id}")
    @ApiOperation(value = "Creator of the task can approve applications",
            response = Unit::class,
            httpMethod = "POST",
            authorizations = [Authorization(value = "basicAuth")])
    @ApiResponses(ApiResponse(code = 204, message = "No Content", response = Unit::class))
    fun approveApplication(@PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity(service.changeApplicationStatus(id, true), HttpStatus.NO_CONTENT)
    }

    @PostMapping("/disapprove/{id}")
    @ApiOperation(value = "Creator of the task can disapprove applications",
            response = Unit::class,
            httpMethod = "POST",
            authorizations = [Authorization(value = "basicAuth")])
    @ApiResponses(ApiResponse(code = 204, message = "No Content", response = Unit::class))
    fun disapproveApplication(@PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity(service.changeApplicationStatus(id, false), HttpStatus.NO_CONTENT)
    }

}
