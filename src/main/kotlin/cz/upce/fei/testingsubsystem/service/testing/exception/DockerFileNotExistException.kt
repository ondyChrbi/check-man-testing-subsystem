package cz.upce.fei.testingsubsystem.service.testing.exception

import java.nio.file.Path

class DockerFileNotExistException(message: String) : Exception(message) {
    constructor(dockerFilePath: Path) : this(
        """Docker file not found $dockerFilePath""".trimIndent()
    )
}