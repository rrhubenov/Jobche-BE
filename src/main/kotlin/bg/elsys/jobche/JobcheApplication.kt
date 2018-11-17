package bg.elsys.jobche

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JobcheApplication

fun main(args: Array<String>) {
    runApplication<JobcheApplication>(*args)
}
