package cz.upce.fei.testingsubsystem.service.testing.exception

import cz.upce.fei.testingsubsystem.domain.testing.TestConfiguration

class TestModuleNotSetException(configuration: TestConfiguration) : Exception("""
    Test module not set for configuration $configuration.
    Please set test module in configuration or use REST API.
""".trimIndent())
