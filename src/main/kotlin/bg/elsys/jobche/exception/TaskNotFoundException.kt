package bg.elsys.jobche.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class TaskNotFoundException(message: String = "Exception: The requested task was not found") : RuntimeException(message) {

}
