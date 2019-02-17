package bg.elsys.jobche.entity.response.user

import bg.elsys.jobche.entity.model.user.ReviewGrade

data class ReviewResponse(val id: Long?,
                          val workId: Long?,
                          val userId: Long?,
                          val reviewGrade: ReviewGrade?)
