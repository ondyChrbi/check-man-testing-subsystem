package cz.upce.fei.testingsubsystem.domain

import cz.upce.fei.testingsubsystem.dto.TestConfigurationDto
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class TestConfiguration(
    @field:Id @field:GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @field:Column var templatePath: String? = null,
    @field:Column var dockerFilePath: String? = null,
    @field:Column var active: Boolean = true,
    @field:Column var creationDate: LocalDateTime = LocalDateTime.now(),
    @field:Column var updateDate: LocalDateTime? = null,
    @field:OneToOne
    @field:JoinColumn(name = "challenge_id", nullable = false)
    var challenge: Challenge? = null
) {
    fun toDto(): TestConfigurationDto {
        return TestConfigurationDto(
            id = id!!, templatePath = templatePath, dockerFilePath = dockerFilePath, active = active, creationDate = creationDate, updateDate = updateDate
        )
    }

    override fun toString(): String {
        return "TestConfiguration(id=$id, templatePath=$templatePath, dockerFilePath=$dockerFilePath, active=$active, creationDate=$creationDate, updateDate=$updateDate)"
    }


}
