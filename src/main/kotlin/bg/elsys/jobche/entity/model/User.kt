package bg.elsys.jobche.entity.model

import bg.elsys.jobche.entity.BaseEntity
import io.swagger.annotations.ApiModelProperty
import javax.persistence.*


@Entity
@Table(name = "Users")
data class User(@ApiModelProperty(required = true)
                            var firstName: String = "",
                            @ApiModelProperty(required = true)
                            var lastName: String = "",
                            @ApiModelProperty(required = true)
                            var email: String = "",
                            @ApiModelProperty(required = true)
                            var password: String = "") : BaseEntity()
