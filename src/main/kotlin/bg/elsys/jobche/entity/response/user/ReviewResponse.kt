package bg.elsys.jobche.entity.response.user

import bg.elsys.jobche.entity.model.user.ReviewGrade

data class ReviewResponse(val id: Long?,
                          val workId: Long?,
                          val reviewGrade: ReviewGrade?,
                          val comment: String?,
                          val reviewer: ReviewerResponse?)

data class ReviewerResponse(val id: Long?,
                            val firstName: String?,
                            val lastName: String?,
                            val profilePicture: String? = null)