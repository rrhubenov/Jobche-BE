package bg.elsys.jobche.entity.body

data class UserRegisterBody(val firstName: String,
                            val lastName: String,
                            val email: String,
                            val password: String)
