package cz.upce.fei.testingsubsystem.service.testing

import cz.upce.fei.testingsubsystem.domain.testing.TestConfiguration

class DockerFileNotSetException(configuration: TestConfiguration) : Exception("""
    Docker file not set for configuration $configuration
""".trimIndent())
