package cz.upce.fei.testingsubsystem.dto

import cz.upce.fei.testingsubsystem.domain.testing.TestConfiguration

data class TestConfigurationInputDtoV1(var testModuleClass: String? = null) {
    fun toEntity(): TestConfiguration {
        return TestConfiguration(
            testModuleClass = testModuleClass)
    }
}
