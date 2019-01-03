package bg.elsys.jobche.service

import bg.elsys.jobche.entity.body.task.TaskBody
import bg.elsys.jobche.entity.model.Task
import bg.elsys.jobche.entity.response.TaskResponse
import bg.elsys.jobche.exceptions.TaskNotFoundException
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

    fun getTask(id: Long): TaskResponse {
        if(taskRepository.existsById(id)) {
            val task = taskRepository.findById(id).get()
            return TaskResponse(task.id,
                    task.title,
                    task.description,
                    task.payment,
                    task.numberOfWorkers,
                    task.dateTime)
        } else throw TaskNotFoundException()
    }

    fun updateTask(task: TaskBody, id: Long) {
        if(taskRepository.existsById(id)) {
            val taskToUpdate = taskRepository.getOne(id)
            taskToUpdate.title = task.title
            taskToUpdate.payment = task.payment
            taskToUpdate.numberOfWorkers = task.numberOfWorkers
            taskToUpdate.description = task.description
            taskToUpdate.dateTime = task.dateTime
            taskRepository.save(taskToUpdate)
        } else throw TaskNotFoundException()
    }

}
