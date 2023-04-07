package cz.upce.fei.testingsubsystem.service.testing.exception

import cz.upce.fei.testingsubsystem.domain.testing.TestConfiguration

class DockerFileNotSetException(message: String) : Exception(message) {
    constructor(configuration: TestConfiguration) : this("""
        Docker file not set for configuration $configuration
    """.trimIndent())
    constructor(c: Class<*>) : this(c.name)
}