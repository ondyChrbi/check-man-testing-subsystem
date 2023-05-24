package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.testing.Feedback
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedbackRepository : JpaRepository<Feedback, Long> {
    fun findFirstByDescriptionEquals(description: String) : Feedback?
}