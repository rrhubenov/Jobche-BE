package bg.elsys.jobche.service

import bg.elsys.jobche.config.security.AuthenticationDetails
import bg.elsys.jobche.converter.Converters
import bg.elsys.jobche.entity.body.user.ReviewBody
import bg.elsys.jobche.entity.model.user.Review
import bg.elsys.jobche.entity.model.work.WorkStatus
import bg.elsys.jobche.entity.response.user.ReviewResponse
import bg.elsys.jobche.exception.ResourceForbiddenException
import bg.elsys.jobche.exception.ResourceNotFoundException
import bg.elsys.jobche.repository.ReviewRepository
import bg.elsys.jobche.repository.UserRepository
import bg.elsys.jobche.repository.WorkRepository
import org.springframework.stereotype.Service

@Service
class ReviewService(val reviewRepository: ReviewRepository, val workRepository: WorkRepository, val userRepository: UserRepository, val authenticationDetails: AuthenticationDetails, val converters: Converters) {
    fun create(reviewBody: ReviewBody): ReviewResponse {
        val optionalWork = workRepository.findById(reviewBody.workId)
        val optionalUser = userRepository.findById(reviewBody.userId)

        if (optionalWork.isPresent && optionalUser.isPresent) {
            val work = optionalWork.get()

            if (work.status != WorkStatus.ENDED) {
                throw ResourceForbiddenException("Exception: You do not have permission to create a review for a work that has not ended")
            }

            val graded = optionalUser.get()

            val grader = authenticationDetails.getUser()

            //Check that the graded user was a participant of the work && the grader is the work creator
            if (work.task.creator.id == grader.id
                    && work.participations.any { it.user.id == graded.id }) {
                val review = reviewRepository.save(Review(graded, work, reviewBody.reviewGrade, reviewBody.comment))

                return with(converters) {
                    review.response
                }

            } else throw ResourceForbiddenException("Exception: You do not have permission to grade this user for this work." +
                    " You are either not the creator for this work or the the user you are trying to grade is not a participant in this work")
        } else throw ResourceNotFoundException()
    }
}
