package cz.upce.fei.testingsubsystem.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import java.time.LocalDateTime

@Entity
data class Challenge (
    @field:Id var id: Long,
    @field:Column var name: String = "",
    @field:Column var description: String = "",
    @field:Column var deadlineDate: LocalDateTime? = null,
    @field:Column var startDate: LocalDateTime? = null,
    @field:Column var active: Boolean = true,
    @field:Column var published: Boolean = false,
    @field:OneToOne(mappedBy = "challenge")
    var testConfiguration: TestConfiguration? = null
) {
    override fun toString(): String {
        return "Challenge(id=$id, name='$name', description='$description', deadlineDate=$deadlineDate, startDate=$startDate, active=$active, published=$published)"
    }
}
