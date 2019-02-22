package bg.elsys.jobche.controller

import bg.elsys.jobche.entity.body.user.ReviewBody
import bg.elsys.jobche.entity.response.user.ReviewResponse
import bg.elsys.jobche.service.ReviewService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reviews")
class ReviewController(val reviewService: ReviewService) {
    @PostMapping
    fun create(@RequestBody reviewBody: ReviewBody): ResponseEntity<ReviewResponse> {
        return ResponseEntity(reviewService.create(reviewBody), HttpStatus.CREATED)
    }


}