package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.body.WorkBody
import bg.elsys.jobche.entity.response.WorkResponse
import bg.elsys.jobche.service.WorkService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/work")
class WorkController(private val service: WorkService) {

    @PostMapping
    fun create(@RequestBody workBody: WorkBody): ResponseEntity<WorkResponse> {
        return ResponseEntity(service.create(workBody), HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity(service.delete(id), HttpStatus.OK)
    }

}
