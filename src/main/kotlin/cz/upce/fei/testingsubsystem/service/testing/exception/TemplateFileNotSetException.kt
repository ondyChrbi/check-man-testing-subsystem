package cz.upce.fei.testingsubsystem.service.testing.exception

import cz.upce.fei.testingsubsystem.domain.testing.TestConfiguration

class TemplateFileNotSetException(configuration: TestConfiguration) : Exception("""
    Template file not set for configuration $configuration
""".trimIndent())