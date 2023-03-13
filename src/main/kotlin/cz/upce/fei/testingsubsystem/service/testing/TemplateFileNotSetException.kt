package cz.upce.fei.testingsubsystem.service.testing

import cz.upce.fei.testingsubsystem.domain.TestConfiguration

class TemplateFileNotSetException(configuration: TestConfiguration) : Exception("""
    Template file not set for configuration $configuration
""".trimIndent())