package bg.elsys.jobche.entity.response.application

data class ApplicationResponse(val id: Long?,
                               val applicantId: Long?,
                               val taskId: Long?,
                               var accepted: Boolean)
