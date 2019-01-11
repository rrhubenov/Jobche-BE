package bg.elsys.jobche.entity.response

import bg.elsys.jobche.entity.model.Task

data class TaskPaginatedResponse(val tasks: List<TaskResponse>)