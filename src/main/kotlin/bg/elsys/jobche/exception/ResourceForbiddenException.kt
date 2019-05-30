package bg.elsys.jobche.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class ResourceForbiddenException(message: String = "Exception: Resource forbidden for modification") : RuntimeException(message)