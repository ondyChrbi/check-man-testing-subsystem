package cz.upce.fei.testingsubsystem.service.solution

import cz.upce.fei.testingsubsystem.domain.testing.Feedback
import cz.upce.fei.testingsubsystem.domain.testing.Review
import cz.upce.fei.testingsubsystem.domain.testing.Solution
import cz.upce.fei.testingsubsystem.repository.FeedbackRepository
import cz.upce.fei.testingsubsystem.repository.ReviewRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewService(
    private val feedbackRepository: FeedbackRepository,
    private val reviewRepository: ReviewRepository
) {
    @Transactional
    fun createReviewIfNotExist(solution: Solution): Review {
        var review = reviewRepository.findBySolutionEquals(solution)

        if (review == null) {
            review = reviewRepository.save(Review(solution = solution))
        }

        return review
    }

    @Transactional
    fun assignFeedbacks(review: Review, feedbacks: Collection<Feedback>): Review {
        review.feedbacks.addAll(feedbacks.map {
            val feedback = feedbackRepository.findFirstByDescriptionEquals(it.description) ?: feedbackRepository.save(it)
            feedback.reviews.add(review)
            feedbackRepository.save(feedback)
        })


        return reviewRepository.saveAndFlush(review)
    }
}