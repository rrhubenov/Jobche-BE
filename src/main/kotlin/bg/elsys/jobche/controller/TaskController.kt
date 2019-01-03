package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.body.task.TaskBody
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
    fun createTask(@RequestBody taskBody: TaskBody): ResponseEntity<TaskResponse> {
        return ResponseEntity(taskService.createTask(taskBody), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long): ResponseEntity<TaskResponse> {
        return ResponseEntity(taskService.getTask(id), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    fun updateTask(@PathVariable id: Long, @RequestBody taskBody: TaskBody): ResponseEntity<Unit> {
        return ResponseEntity(taskService.updateTask(taskBody, id), HttpStatus.OK)
    }
}