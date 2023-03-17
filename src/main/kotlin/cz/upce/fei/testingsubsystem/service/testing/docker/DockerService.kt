package cz.upce.fei.testingsubsystem.service.testing.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.command.BuildImageCmd
import com.github.dockerjava.api.command.BuildImageResultCallback
import com.github.dockerjava.api.command.WaitContainerResultCallback
import com.github.dockerjava.api.model.*
import cz.upce.fei.testingsubsystem.component.testing.GradleModule
import cz.upce.fei.testingsubsystem.domain.Solution
import cz.upce.fei.testingsubsystem.domain.TestResult
import cz.upce.fei.testingsubsystem.repository.TestResultRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.Closeable
import java.nio.file.Path

@Service
@Scope("prototype")
class DockerService(
    private val testResultRepository: TestResultRepository,
    private val applicationContext: ApplicationContext,
    private val hostConfig: HostConfig,

) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun createImage(buildImageCmd: BuildImageCmd, testResult: TestResult?, log: StringBuffer): String? {
        return buildImageCmd.exec(object : BuildImageResultCallback() {
            override fun onStart(stream: Closeable?) {
                if (testResult != null) {
                    changeState(testResult, Solution.TestStatus.RUNNING)
                }
                super.onStart(stream)
            }

            override fun onNext(item: BuildResponseItem?) {
                logger.info(item?.stream)
                log.append(item?.stream)
                super.onNext(item)
            }

            override fun onComplete() {
                if (testResult != null) {
                    saveLog(testResult, log)
                }
                super.onComplete()
            }

            override fun onError(throwable: Throwable?) {
                log.append(throwable?.message)
                if (testResult != null) {
                    saveLog(testResult, log)
                }
                super.onError(throwable)
            }
        }).awaitImageId()
    }

    fun runContainer(imageId: String, testResult: TestResult? = null, binds: List<Bind> = listOf(),
                     timeout: Int = DEFAULT_CONTAINER_TIMEOUT, log: StringBuffer = StringBuffer("")): String {
        val dockerClient = applicationContext.getBean(DockerClient::class.java)

        val container = dockerClient.createContainerCmd("$imageId:latest")
            .withCmd(GradleModule.DockerCommands.GRADLE_TEST)
            .withHostConfig(hostConfig)
            .withStopTimeout(Int.MAX_VALUE)
            .withVolumes(binds.map { it.volume })
            .withStopTimeout(timeout)
            .withBinds(binds)
            .withTty(true)
            .exec()

        dockerClient.logContainerCmd(container.id)
            .withStdOut(true)
            .withStdErr(true)
            .withFollowStream(true)
            .exec(object : ResultCallback.Adapter<Frame>() {
                override fun onNext(frame: Frame) {
                    logger.info(String(frame.payload))
                }
            })

        dockerClient.startContainerCmd(container.id)
            .exec()
        dockerClient.waitContainerCmd(container.id).exec(object : WaitContainerResultCallback(){})
            .awaitCompletion()

        dockerClient.close()
        return container.id
    }

    fun buildImage(dockerFile: Path, imageName: String, testResult: TestResult?, log: StringBuffer = StringBuffer("")): String {
        val dockerClient = applicationContext.getBean(DockerClient::class.java)

        val buildImageCmd = dockerClient.buildImageCmd(dockerFile.toFile())
            .withTags(mutableSetOf(imageName))

        val image = createImage(buildImageCmd, testResult, log)
            ?: throw DockerImageCreationFailedException()

        dockerClient.close()
        return image
    }

    fun isAvailable() : Boolean {
        val dockerClient = applicationContext.getBean(DockerClient::class.java)

        try {
            dockerClient.pingCmd().exec()
        } catch (e: Exception) {
            return false
        } finally {
            dockerClient.close()
        }

        return true
    }

    fun isNotAvailable() = !isAvailable()

    @Transactional
    protected fun saveLog(testResult: TestResult, log: StringBuffer) {
        testResult.log = log.toString()
        testResultRepository.save(testResult)
    }

    @Transactional
    protected fun changeState(testResult: TestResult, testStatus: Solution.TestStatus = Solution.TestStatus.RUNNING) {
        testResult.testStatusId = testStatus.id
        testResultRepository.save(testResult)
    }

    private companion object {
        const val DEFAULT_CONTAINER_TIMEOUT = 30
    }
}