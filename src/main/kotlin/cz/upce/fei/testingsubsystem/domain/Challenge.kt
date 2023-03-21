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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Challenge

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (deadlineDate != other.deadlineDate) return false
        if (startDate != other.startDate) return false
        if (active != other.active) return false
        if (published != other.published) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (deadlineDate?.hashCode() ?: 0)
        result = 31 * result + (startDate?.hashCode() ?: 0)
        result = 31 * result + active.hashCode()
        result = 31 * result + published.hashCode()
        return result
    }


}
