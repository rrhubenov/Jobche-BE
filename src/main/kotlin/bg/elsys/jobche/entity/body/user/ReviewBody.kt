package bg.elsys.jobche.entity.body.user

import bg.elsys.jobche.entity.model.user.ReviewGrade
import io.swagger.annotations.ApiModelProperty

data class ReviewBody(

        @ApiModelProperty(value = "Id of the work that the user is being graded on", example = "1")
        val workId: Long,

        @ApiModelProperty(value = "Id of the user that is being graded", example = "1")
        val userId: Long,

        @ApiModelProperty(value = "The grade for the user's work", example = "GOOD", allowableValues = "BAD, AVERAGE, GOOD, GREAT, PERFECT")
        val reviewGrade: ReviewGrade)