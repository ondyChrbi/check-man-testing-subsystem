package cz.upce.fei.testingsubsystem.service.testing.docker

class DockerNotAvailableException : Exception("""
    Docker is not available on this machine. Check if docker service is running.
""".trimIndent())
