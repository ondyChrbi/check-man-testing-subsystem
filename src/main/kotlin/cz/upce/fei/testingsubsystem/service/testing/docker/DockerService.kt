package cz.upce.fei.testingsubsystem.service.testing.docker

import com.github.dockerjava.api.DockerClient
import cz.upce.fei.testingsubsystem.domain.TestResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.nio.file.Path
import java.util.*

@Service
@Scope("prototype")
class DockerService(
    private val gradleService: GradleService,
    private val dockerClient: DockerClient,
) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun gradleTest(dockerFile: Path, testResult: TestResult? = null) {
        val imageName = "$GRADLE_CONTAINER_PREFIX-${UUID.randomUUID()}"

        val buildImageCmd = dockerClient.buildImageCmd(dockerFile.toFile())
            .withTags(mutableSetOf(imageName))

        val log = StringBuffer("")
        val imageId = gradleService.createGradleImage(buildImageCmd, testResult, log)
            ?: throw DockerImageCreationFailedException()

        gradleService.runGradleContainer(imageName, testResult, log)

        logger.debug("Container for docker file $dockerFile create with id: $imageId")
    }

    object DockerCommands {
        val GRADLE_TEST = "ls"
    }

    private companion object {
        const val GRADLE_CONTAINER_PREFIX = "checkman-gradle"
    }
}