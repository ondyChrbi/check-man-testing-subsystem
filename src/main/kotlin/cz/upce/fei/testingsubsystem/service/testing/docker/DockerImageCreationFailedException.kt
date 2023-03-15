package cz.upce.fei.testingsubsystem.service.testing.docker

class DockerImageCreationFailedException : Exception("""
    Creation of docker container failed. No image created.
""".trimIndent())
