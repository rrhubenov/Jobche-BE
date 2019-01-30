package bg.elsys.jobche

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer


@SpringBootApplication
class JobcheApplication : SpringBootServletInitializer() {
    override fun configure(builder: SpringApplicationBuilder?): SpringApplicationBuilder {
        return builder!!.sources(JobcheApplication::class.java)
    }
}

fun main(args: Array<String>) {
    runApplication<JobcheApplication>(*args)
}

