package cz.upce.fei.testingsubsystem.domain.testing

import cz.upce.fei.testingsubsystem.domain.course.Challenge
import cz.upce.fei.testingsubsystem.dto.TestConfigurationDtoV1
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table
data class TestConfiguration(
    @field:Id @field:GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @field:Column var templatePath: String? = null,
    @field:Column var dockerFilePath: String? = null,
    @field:Column var testModuleClass: String? = null,
    @field:Column var active: Boolean = true,
    @field:Column var creationDate: LocalDateTime = LocalDateTime.now(),
    @field:Column var updateDate: LocalDateTime? = null,
    @field:OneToOne
    @field:JoinColumn(name = "challenge_id", nullable = false)
    var challenge: Challenge? = null
) {
    fun toDto(): TestConfigurationDtoV1 {
        return TestConfigurationDtoV1(
            id = id!!,
            templatePath = templatePath,
            dockerFilePath = dockerFilePath,
            active = active,
            creationDate = creationDate,
            updateDate = updateDate,
            testModuleClass = testModuleClass
        )
    }

    override fun toString(): String {
        return "TestConfiguration(id=$id, templatePath=$templatePath, dockerFilePath=$dockerFilePath, active=$active, creationDate=$creationDate, updateDate=$updateDate)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TestConfiguration

        if (id != other.id) return false
        if (templatePath != other.templatePath) return false
        if (dockerFilePath != other.dockerFilePath) return false
        if (active != other.active) return false
        if (creationDate != other.creationDate) return false
        if (updateDate != other.updateDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (templatePath?.hashCode() ?: 0)
        result = 31 * result + (dockerFilePath?.hashCode() ?: 0)
        result = 31 * result + active.hashCode()
        result = 31 * result + creationDate.hashCode()
        result = 31 * result + (updateDate?.hashCode() ?: 0)
        return result
    }


}