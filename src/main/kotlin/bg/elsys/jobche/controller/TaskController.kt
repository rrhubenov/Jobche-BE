package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.response.application.ApplicationPaginatedResponse
import bg.elsys.jobche.entity.response.application.ApplicationResponse
import bg.elsys.jobche.entity.response.task.TaskPaginatedResponse
import bg.elsys.jobche.entity.response.task.TaskResponse
import bg.elsys.jobche.service.ApplicationService
import bg.elsys.jobche.service.TaskService
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(value = "Task Operations", description = "All operations for tasks")
@RestController
@RequestMapping("/tasks")
class TaskController(val taskService: TaskService, val applicationService: ApplicationService) {

    @PostMapping
    @ApiOperation(value = "Create task",
            response = TaskResponse::class,
            httpMethod = "POST",
            authorizations = arrayOf(Authorization(value="basicAuth")))
    @ApiResponses(ApiResponse(code = 201, message = "Success", response = TaskResponse::class))
    fun create(@RequestBody taskBody: TaskBody): ResponseEntity<TaskResponse> {
        return ResponseEntity(taskService.create(taskBody), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Read one task",
            httpMethod = "GET",
            authorizations = arrayOf(Authorization(value="basicAuth")))
    @ApiResponses(ApiResponse(code = 200, message = "Success", response = TaskResponse::class))
    fun read(@PathVariable id: Long): ResponseEntity<TaskResponse> {
        return ResponseEntity(taskService.read(id), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update task",
            httpMethod = "PUT",
            authorizations = arrayOf(Authorization(value="basicAuth")))
    @ApiResponses(ApiResponse(code = 200, message = "Success", response = Unit::class))
    fun update(@PathVariable id: Long, @RequestBody taskBody: TaskBody): ResponseEntity<Unit> {
        return ResponseEntity(taskService.update(taskBody, id), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete task",
            httpMethod = "DELETE",
            authorizations = arrayOf(Authorization(value="basicAuth")))
    @ApiResponses(ApiResponse(code = 204, message = "No Content", response = Unit::class))
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity(taskService.delete(id), HttpStatus.NO_CONTENT)
    }

    @GetMapping
    @ApiOperation(value = "Read tasks paginated",
            httpMethod = "GET",
            authorizations = arrayOf(Authorization(value="basicAuth")))
    @ApiResponses(ApiResponse(code = 200, message = "Success", response = TaskPaginatedResponse::class))
    fun readPaginated(@RequestParam("page") page: Int, @RequestParam("size") size: Int): ResponseEntity<TaskPaginatedResponse> {
        val tasks = taskService.readPaginated(page, size)

        val taskResponses = tasks.map {
            TaskResponse(it.id, it.title, it.description, it.payment, it.numberOfWorkers, it.dateTime, it.location, it.creatorId)
        }.toMutableList()

        return ResponseEntity(TaskPaginatedResponse(taskResponses), HttpStatus.OK)
    }

    @GetMapping("/{taskId}/applications")
    @ApiOperation(value = "Get applications for task",
            httpMethod = "GET",
            authorizations = arrayOf(Authorization(value="basicAuth")))
    @ApiResponses(ApiResponse(code = 200, message = "Success", response = ApplicationPaginatedResponse::class))
    fun getApplications(@PathVariable taskId: Long, @RequestParam("page") page: Int, @RequestParam("size") size: Int): ResponseEntity<ApplicationPaginatedResponse> {
        val applicationList = applicationService.getApplicationsForTask(taskId, page, size)
        val applicationResponseList = mutableListOf<ApplicationResponse>()

        for(app in applicationList) {
            applicationResponseList.add(ApplicationResponse(app.id, app.user.id, app.task.id, app.accepted))
        }

        return ResponseEntity(ApplicationPaginatedResponse(applicationResponseList), HttpStatus.OK)
    }
}