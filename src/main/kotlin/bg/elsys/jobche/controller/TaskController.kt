package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.response.TaskResponse
import bg.elsys.jobche.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tasks")
class TaskController(val taskService: TaskService) {
    @PostMapping
    fun createTask(@RequestBody taskBody: TaskBody): ResponseEntity<TaskResponse> {
        return ResponseEntity(taskService.createTask(taskBody), HttpStatus.CREATED)
    }
}
