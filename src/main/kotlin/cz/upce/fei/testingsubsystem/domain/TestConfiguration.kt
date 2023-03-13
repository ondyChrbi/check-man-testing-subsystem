package cz.upce.fei.testingsubsystem.domain

import cz.upce.fei.testingsubsystem.dto.TestConfigurationDto
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import java.time.LocalDateTime

@Entity
data class TestConfiguration(
    @field:Id @field:GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    var templatePath: String? = null,
    var dockerFilePath: String? = null,
    var active: Boolean = true,
    var creationDate: LocalDateTime = LocalDateTime.now(),
    var updateDate: LocalDateTime? = null,
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
