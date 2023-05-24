package cz.upce.fei.testingsubsystem.domain.testing

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table
data class TestResult(
    @field:Id @field:GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @field:Column var log: String = "",
    @field:Column var creationDate: LocalDateTime = LocalDateTime.now(),
    @field:Column var updateDate: LocalDateTime? = null,
    @field:Column var testStatusId: Long = Solution.TestStatus.WAITING_TO_TEST.id,
    @field:ManyToOne(fetch = FetchType.EAGER)
    @field:JoinColumn(name = "solution_id", nullable = true)
    var solution: Solution? = null
)