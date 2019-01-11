package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.response.TaskPaginatedResponse
import bg.elsys.jobche.entity.response.TaskResponse
import bg.elsys.jobche.service.TaskService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.Authorization
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(val taskService: TaskService) {

    @PostMapping
    @ApiOperation(value = "Create task",
            response = TaskResponse::class,
            httpMethod = "POST",
            authorizations = arrayOf(Authorization(value="basicAuth")))
    fun create(@RequestBody taskBody: TaskBody): ResponseEntity<TaskResponse> {
        return ResponseEntity(taskService.create(taskBody), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Long): ResponseEntity<TaskResponse> {
        return ResponseEntity(taskService.read(id), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody taskBody: TaskBody): ResponseEntity<Unit> {
        return ResponseEntity(taskService.update(taskBody, id), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity(taskService.delete(id), HttpStatus.NO_CONTENT)
    }

    @GetMapping
    fun readPaginated(@RequestParam("page") page: Int, @RequestParam("size") size: Int): ResponseEntity<TaskPaginatedResponse> {
        val tasks = taskService.readPaginated(page, size)

        val taskResponses = tasks.map {
            TaskResponse(it.id, it.title, it.description, it.payment, it.numberOfWorkers, it.dateTime)
        }.toMutableList()

        return ResponseEntity(TaskPaginatedResponse(taskResponses), HttpStatus.OK)
    }
}