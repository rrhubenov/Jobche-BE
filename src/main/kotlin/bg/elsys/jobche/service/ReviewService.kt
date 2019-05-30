package bg.elsys.jobche.service

import bg.elsys.jobche.config.security.AuthenticationDetails
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
class ReviewService(val reviewRepository: ReviewRepository, val workRepository: WorkRepository, val userRepository: UserRepository, val authenticationDetails: AuthenticationDetails) {
    fun create(reviewBody: ReviewBody): ReviewResponse {
        val optionalWork = workRepository.findById(reviewBody.workId)
        val optionalUser = userRepository.findById(reviewBody.userId)

        if (optionalWork.isPresent && optionalUser.isPresent) {
            val work = optionalWork.get()

            if (!work.status.equals(WorkStatus.ENDED)) {
                throw ResourceForbiddenException("Exception: You do not have permission to create a review for an ended work")
            }

            val user = optionalUser.get()

            val requestingUser = userRepository.findByEmail(authenticationDetails.getEmail())

            //Check that the graded user was a participant of the work && the grader is the work creator
            if (work.task.creator.id == requestingUser?.id
                    && work.participations.stream().anyMatch { it.user.id == user.id }) {

                val review = reviewRepository.save(Review(user, work, reviewBody.reviewGrade))

                return ReviewResponse(review.id, review.work.id, review.reviewGrade)

            } else throw ResourceForbiddenException("Exception: You do not have permission to grade this user for this work." +
                    " You are either not the creator for this work or the the user you are trying to grade is not a participant in this work")
        } else throw ResourceNotFoundException()
    }
}
