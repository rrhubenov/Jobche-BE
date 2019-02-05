package bg.elsys.jobche.entity.body.task

import javax.persistence.Embeddable

@Embeddable
data class Address(val country: String = "",
                   val city: String = "")