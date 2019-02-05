package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.body.task.Address
import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.model.task.PaymentType
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
import java.time.LocalDateTime

@Api(value = "Task Operations", description = "All operations for tasks")
@RestController
@RequestMapping("/tasks")
class TaskController(val taskService: TaskService, val applicationService: ApplicationService) {

    @PostMapping
    @ApiOperation(value = "Create task",
            response = TaskResponse::class,
            httpMethod = "POST",
            authorizations = arrayOf(Authorization(value = "basicAuth")))
    @ApiResponses(ApiResponse(code = 201, message = "Success", response = TaskResponse::class))
    fun create(@RequestBody taskBody: TaskBody): ResponseEntity<TaskResponse> {
        val task = taskService.create(taskBody)
        val taskResponse = TaskResponse(task.id,
                task.title,
                task.description,
                task.payment,
                task.numberOfWorkers,
                task.dateTime,
                task.location,
                task.creatorId,
                task.paymentType
        )

        return ResponseEntity(taskResponse, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Read one task",
            httpMethod = "GET",
            authorizations = arrayOf(Authorization(value = "basicAuth")))
    @ApiResponses(ApiResponse(code = 200, message = "Success", response = TaskResponse::class))
    fun read(@PathVariable id: Long): ResponseEntity<TaskResponse> {
        val task = taskService.read(id)
        val taskResponse = TaskResponse(task.id,
                task.title,
                task.description,
                task.payment,
                task.numberOfWorkers,
                task.dateTime,
                task.location,
                task.creatorId,
                task.paymentType)

        return ResponseEntity(taskResponse, HttpStatus.OK)

    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update task",
            httpMethod = "PUT",
            authorizations = arrayOf(Authorization(value = "basicAuth")))
    @ApiResponses(ApiResponse(code = 200, message = "Success", response = Unit::class))
    fun update(@PathVariable id: Long, @RequestBody taskBody: TaskBody): ResponseEntity<Unit> {
        return ResponseEntity(taskService.update(taskBody, id), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete task",
            httpMethod = "DELETE",
            authorizations = arrayOf(Authorization(value = "basicAuth")))
    @ApiResponses(ApiResponse(code = 204, message = "No Content", response = Unit::class))
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity(taskService.delete(id), HttpStatus.NO_CONTENT)
    }

    @GetMapping
    @ApiOperation(value = "Read tasks paginated",
            httpMethod = "GET",
            authorizations = arrayOf(Authorization(value = "basicAuth")))
    @ApiResponses(ApiResponse(code = 200, message = "Success", response = TaskPaginatedResponse::class))
    fun readPaginated(@RequestParam("page") page: Int,
                      @RequestParam("size") size: Int,
                      @RequestParam("title", required = false) title: String? = null,
                      @RequestParam("pStart", required = false) paymentStart: Int? = null,
                      @RequestParam("numWStart", required = false) numWStart: Int? = null,
                      @RequestParam("dateStart", required = false) dateStart: LocalDateTime? = null,
                      @RequestParam("location", required = false) location: Address? = null,
                      @RequestParam("pType", required = false) pType: PaymentType? = null): ResponseEntity<TaskPaginatedResponse> {

        val tasks = taskService.readPaginated(page, size, title, paymentStart, numWStart, dateStart, location, pType)

        val taskResponses = tasks.map {
            TaskResponse(it.id, it.title, it.description, it.payment, it.numberOfWorkers, it.dateTime, it.location, it.creatorId, it.paymentType)
        }.toMutableList()

        return ResponseEntity(TaskPaginatedResponse(taskResponses), HttpStatus.OK)
    }

    @GetMapping("/{taskId}/applications")
    @ApiOperation(value = "Get applications for task",
            httpMethod = "GET",
            authorizations = arrayOf(Authorization(value = "basicAuth")))
    @ApiResponses(ApiResponse(code = 200, message = "Success", response = ApplicationPaginatedResponse::class))
    fun getApplications(@PathVariable taskId: Long, @RequestParam("page") page: Int, @RequestParam("size") size: Int): ResponseEntity<ApplicationPaginatedResponse> {
        val applicationList = applicationService.getApplicationsForTask(taskId, page, size)
        val applicationResponseList = mutableListOf<ApplicationResponse>()

        for (app in applicationList) {
            applicationResponseList.add(ApplicationResponse(app.id, app.user.id, app.task.id, app.accepted))
        }

        return ResponseEntity(ApplicationPaginatedResponse(applicationResponseList), HttpStatus.OK)
    }

    @GetMapping("/me")
    @ApiOperation(value = "Read my tasks paginated",
            httpMethod = "GET",
            authorizations = arrayOf(Authorization(value = "basicAuth")))
    @ApiResponses(ApiResponse(code = 200, message = "Success", response = TaskPaginatedResponse::class))
    fun readMePaginated(@RequestParam("page") page: Int, @RequestParam("size") size: Int): ResponseEntity<TaskPaginatedResponse> {
        val tasks = taskService.readMePaginated(page, size)

        val taskResponses = tasks.map {
            TaskResponse(it.id, it.title, it.description, it.payment, it.numberOfWorkers, it.dateTime, it.location, it.creatorId, it.paymentType)
        }.toMutableList()

        return ResponseEntity(TaskPaginatedResponse(taskResponses), HttpStatus.OK)
    }
}