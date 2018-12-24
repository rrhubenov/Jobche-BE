package bg.elsys.jobche.service

import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.model.Task
import bg.elsys.jobche.entity.response.TaskResponse
import bg.elsys.jobche.repositories.TaskRepository
import org.springframework.stereotype.Service

@Service
class TaskService(val taskRepository: TaskRepository) {
    fun createTask(taskBody: TaskBody): TaskResponse {
        val task = taskRepository.save(Task(taskBody.title,
                taskBody.description,
                taskBody.payment,
                taskBody.numberOfWorkers,
                taskBody.dateTime))

        return TaskResponse(task.id,
                task.title,
                task.description,
                task.payment,
                task.numberOfWorkers,
                task.dateTime)
    }

}
