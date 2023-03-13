package cz.upce.fei.testingsubsystem.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import java.time.LocalDateTime

@Entity
data class Challenge (
    @field:Id var id: Long,
    var name: String = "",
    var description: String = "",
    var deadlineDate: LocalDateTime? = null,
    var startDate: LocalDateTime? = null,
    var active: Boolean = true,
    var published: Boolean = false,
    @field:OneToOne(mappedBy = "challenge")
    var testConfiguration: TestConfiguration? = null
)
