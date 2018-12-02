package bg.elsys.jobche.entity

import io.swagger.annotations.ApiModelProperty
import javax.persistence.*

@Entity
@Table(name = "Users")
data class User(@ApiModelProperty(required = true)
                val firstName: String = "",
                @ApiModelProperty(required = true)
                val lastName: String = "",
                @ApiModelProperty(required = true)
                val email: String = "") : BaseEntity()
