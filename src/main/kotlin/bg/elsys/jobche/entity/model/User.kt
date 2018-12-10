package bg.elsys.jobche.entity.model

import bg.elsys.jobche.entity.BaseEntity
import io.swagger.annotations.ApiModelProperty
import javax.persistence.*

@Entity
@Table(name = "Users")
data class User(@ApiModelProperty(required = true)
                            val firstName: String = "",
                            @ApiModelProperty(required = true)
                            val lastName: String = "",
                            @ApiModelProperty(required = true)
                            val email: String = "",
                            @ApiModelProperty(required = true)
                            val password: String = "") : BaseEntity()
