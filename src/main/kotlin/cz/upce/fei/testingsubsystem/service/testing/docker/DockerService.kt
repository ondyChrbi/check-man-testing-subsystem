package cz.upce.fei.testingsubsystem.service.testing.docker

import com.github.dockerjava.api.command.BuildImageResultCallback
import com.github.dockerjava.api.model.BuildResponseItem
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import com.github.dockerjava.transport.DockerHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.nio.file.Path
import java.time.Duration
import java.util.*


@Service
@Scope("prototype")
class DockerService {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun gradleTest(dockerFile: Path) {
        val config = DefaultDockerClientConfig.createDefaultConfigBuilder().build()

        val httpClient: DockerHttpClient = ApacheDockerHttpClient.Builder()
            .dockerHost(config.dockerHost)
            .sslConfig(config.sslConfig)
            .maxConnections(100)
            .connectionTimeout(Duration.ofSeconds(30))
            .responseTimeout(Duration.ofSeconds(45))
            .build()

        val dockerClient = DockerClientImpl.getInstance(config, httpClient)

        val imageName = "$GRADLE_CONTAINER_PREFIX-${UUID.randomUUID()}"

        val buildImageCmd = dockerClient.buildImageCmd(dockerFile.toFile())
            .withTags(mutableSetOf(imageName))

        val imageId = buildImageCmd.exec(object : BuildImageResultCallback() {
            override fun onNext(item: BuildResponseItem?) {
                println(item?.stream)
                super.onNext(item)
            }
        }).awaitImageId()

        logger.debug("Container for docker file $dockerFile create with id: $imageId")
    }

    private companion object {
        const val GRADLE_CONTAINER_PREFIX = "checkman-gradle"
    }
}