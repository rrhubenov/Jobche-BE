package bg.elsys.jobche.entity.model.picture

import bg.elsys.jobche.entity.model.task.Task
import javax.persistence.*

@Entity
@Table(name = "taskPictures")
data class TaskPicture(@ManyToOne(fetch = FetchType.EAGER)
                       val task: Task): Picture()