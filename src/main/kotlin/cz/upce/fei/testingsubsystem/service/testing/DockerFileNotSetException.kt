package cz.upce.fei.testingsubsystem.service.testing

import cz.upce.fei.testingsubsystem.domain.TestConfiguration

class DockerFileNotSetException(configuration: TestConfiguration) : Exception("""
    Docker file not set for configuration $configuration
""".trimIndent())
