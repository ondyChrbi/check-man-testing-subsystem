package cz.upce.fei.testingsubsystem.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class TestResult(
    @field:Id @field:GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    var log: String = "",
    var creationDate: LocalDateTime = LocalDateTime.now(),
    var updateDate: LocalDateTime? = null,
    var testStatusId: Long = Solution.TestStatus.WAITING_TO_TEST.id,
    @field:ManyToOne(fetch = FetchType.EAGER)
    @field:JoinColumn(name = "solution_id", nullable = true)
    var solution: Solution? = null
)
