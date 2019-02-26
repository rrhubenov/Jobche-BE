package bg.elsys.jobche.entity.body.user

data class UserBody(val firstName: String,
                    val lastName: String,
                    val email: String,
                    val password: String,
                    val dateOfBirth: DateOfBirth,
                    val phoneNum: String)
