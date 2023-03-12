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
    var challenge : Challenge
) {
    fun toDto(): SolutionDtoV1 {
        return SolutionDtoV1(
            id!!, path, uploadDate
        )
    }

    private companion object {
        const val DEFAULT_STATUS = 3L
        const val DEFAULT_TESTING_STATUS = 0L
    }
}
