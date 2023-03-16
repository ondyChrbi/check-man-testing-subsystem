package cz.upce.fei.testingsubsystem.domain

import cz.upce.fei.testingsubsystem.component.testing.JunitTestCase
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Feedback(
    @field:Id @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @field:Column var description: String = "",
    @field:Column var feedbackTypeId: Long = FeedbackType.EXTREMELY_POSITIVE.id,
    @field:Column var creationDate: LocalDateTime = LocalDateTime.now(),
    @field:ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @field:JoinTable(
        name = "feedback_review",
        joinColumns = [JoinColumn(name = "feedback_id")],
        inverseJoinColumns = [JoinColumn(name = "review_id")]
    )
    var reviews: MutableCollection<Review> = mutableSetOf()
) {


    enum class FeedbackType(val id: Long) {
        EXTREMELY_POSITIVE(0L),
        POSITIVE(1L),
        NEUTRAL(2L),
        NEGATIVE(3L);
    }

    companion object {
        val IDS_MAP = mapOf(
            0L to FeedbackType.EXTREMELY_POSITIVE,
            1L to FeedbackType.POSITIVE,
            2L to FeedbackType.NEUTRAL,
            3L to FeedbackType.NEGATIVE
        )

        val TEST_CASE_MAP = mapOf(
            JunitTestCase.TestStatus.ERRORED to FeedbackType.NEGATIVE,
            JunitTestCase.TestStatus.FAILED to FeedbackType.NEGATIVE,
            JunitTestCase.TestStatus.PASSED to FeedbackType.POSITIVE,
        )

        fun getById(id: Long) = FeedbackType.values()[id.toInt()]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Feedback

        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        return description.hashCode()
    }
}
