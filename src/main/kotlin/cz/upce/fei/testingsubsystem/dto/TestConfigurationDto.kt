package cz.upce.fei.testingsubsystem.dto

import java.time.LocalDateTime

data class TestConfigurationDto (
    var id: Long,
    var templatePath: String? = null,
    var dockerFilePath: String? = null,
    var active: Boolean = true,
    var creationDate: LocalDateTime = LocalDateTime.now(),
    var updateDate: LocalDateTime? = null,
)
