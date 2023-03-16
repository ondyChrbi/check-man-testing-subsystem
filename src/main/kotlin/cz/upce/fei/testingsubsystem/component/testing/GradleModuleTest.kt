package cz.upce.fei.testingsubsystem.component.testing

import com.github.dockerjava.api.model.Bind
import com.github.dockerjava.api.model.Volume
import cz.upce.fei.testingsubsystem.domain.TestResult
import cz.upce.fei.testingsubsystem.service.testing.docker.DockerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.util.*

@Component
@Scope("prototype")
class GradleModuleTest(private val dockerService: DockerService) : ModuleTest {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${check-man.modules.gradle.volume.container-path}")
    private var volumeContainerPath : String = ""

    override fun test(dockerFile: Path, testResult: TestResult?, resultPath: Path) {
        super.test(dockerFile, testResult, resultPath)

        val imageName = "$GRADLE_CONTAINER_PREFIX-${UUID.randomUUID()}"

        val log = StringBuffer("")
        val imageId = dockerService.buildImage(dockerFile, imageName, testResult)

        val binds = listOf(Bind(resultPath.toAbsolutePath().toString(), Volume(volumeContainerPath)))

        dockerService.runContainer(imageName, testResult, binds.toList(), log)
        logger.debug("Container for docker file $dockerFile create with id: $imageId")
    }

    object DockerCommands {
        val GRADLE_TEST = mutableListOf("./gradlew", ":app:test", "--info")
    }

    private companion object {
        const val GRADLE_CONTAINER_PREFIX = "checkman-gradle"
    }
}