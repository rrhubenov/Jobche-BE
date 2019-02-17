package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.body.WorkBody
import bg.elsys.jobche.entity.model.work.WorkStatus
import bg.elsys.jobche.entity.response.WorkResponse
import bg.elsys.jobche.service.WorkService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/work")
class WorkController(private val service: WorkService) {

    @PostMapping
    @ApiOperation(value = "Create a new work in progress",
            httpMethod = "POST")
    @ApiResponses(ApiResponse(code = 201, message = "Created", response = WorkResponse::class))
    fun create(@RequestBody workBody: WorkBody): ResponseEntity<WorkResponse> {
        return ResponseEntity(service.create(workBody), HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes the work",
            httpMethod = "DELETE")
    @ApiResponses(ApiResponse(code = 200, message = "OK", response = Unit::class))
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity(service.delete(id), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Reads the work",
            httpMethod = "READ")
    @ApiResponses(ApiResponse(code = 200, message = "OK", response = WorkResponse::class))
    fun read(@PathVariable id: Long): ResponseEntity<WorkResponse> {
        return ResponseEntity(service.read(id), HttpStatus.OK)
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Changes the status of the Work",
            httpMethod = "PATCH")
    @ApiResponses(ApiResponse(code = 200, message = "OK", response = Unit::class))
    fun changeStatus(@RequestBody workStatus: WorkStatus, @PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity(service.changeStatus(workStatus, id), HttpStatus.OK)
    }

}
