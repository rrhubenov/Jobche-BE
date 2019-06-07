package bg.elsys.jobche.entity.response.user

import bg.elsys.jobche.entity.body.user.DateOfBirth

data class UserResponse(val id: Long?,
                        val firstName: String?,
                        val lastName: String?,
                        val dateOfBirth: DateOfBirth?,
                        val phoneNum: String?,
                        val reviews: List<ReviewResponse>? = emptyList(),
                        val averageReview: Double? = null,
                        val profilePicture: String? = null)