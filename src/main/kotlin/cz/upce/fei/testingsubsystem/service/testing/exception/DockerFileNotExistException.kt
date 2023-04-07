package cz.upce.fei.testingsubsystem.service.testing.exception

import java.nio.file.Path

class DockerFileNotExistException(dockerFilePath: Path) : Exception("""
    Docker file not found $dockerFilePath
""".trimIndent())