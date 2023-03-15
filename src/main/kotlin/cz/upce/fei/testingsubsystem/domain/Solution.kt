package cz.upce.fei.testingsubsystem.domain

import cz.upce.fei.testingsubsystem.dto.SolutionDtoV1
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Solution(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var uploadDate: LocalDateTime = LocalDateTime.now(),
    var path: String,
    @field:ManyToOne(fetch = FetchType.EAGER)
    @field:JoinColumn(name = "user_id", nullable = false)
    var user : AppUser?,
    @field:Column var statusId : Long = DEFAULT_STATUS,
    @field:Column var testStatusId : Long = DEFAULT_TESTING_STATUS,
    @field:ManyToOne(fetch = FetchType.EAGER)
    @field:JoinColumn(name = "challenge_id", nullable = false)
    var challenge : Challenge,
    @field:OneToMany(mappedBy = "solution", fetch = FetchType.LAZY)
    var testResults: List<TestResult> = emptyList()
) {
    fun toDto(): SolutionDtoV1 {
        return SolutionDtoV1(
            id!!, path, uploadDate
        )
    }

    override fun toString(): String {
        return "Solution(id=$id, uploadDate=$uploadDate, path='$path', statusId=$statusId, testStatusId=$testStatusId)"
    }

    private companion object {
        const val DEFAULT_STATUS = 3L
        const val DEFAULT_TESTING_STATUS = 0L
    }

    enum class TestStatus(val id: Long) {
        WAITING_TO_TEST(0L),
        RUNNING(1L),
        FINISHED(2L),
        ERROR(3L);

        companion object {
            val IDS_MAP = mapOf(
                0L to WAITING_TO_TEST,
                1L to RUNNING,
                2L to FINISHED,
                3L to ERROR
            )

            fun getById(id: Long) = values()[id.toInt()]
        }
    }
}
