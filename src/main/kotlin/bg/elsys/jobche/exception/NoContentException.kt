package bg.elsys.jobche.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.NO_CONTENT)
class NoContentException: RuntimeException() {
}