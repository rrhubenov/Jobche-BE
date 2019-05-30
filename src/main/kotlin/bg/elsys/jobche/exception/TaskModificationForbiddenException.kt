package bg.elsys.jobche.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class TaskModificationForbiddenException(message: String = "Exception: The requested task is forbidden for modification") : RuntimeException(message)