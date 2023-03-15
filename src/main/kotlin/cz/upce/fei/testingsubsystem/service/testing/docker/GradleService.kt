package cz.upce.fei.testingsubsystem.service.testing.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.command.BuildImageCmd
import com.github.dockerjava.api.command.BuildImageResultCallback
import com.github.dockerjava.api.command.WaitContainerResultCallback
import com.github.dockerjava.api.model.*
import cz.upce.fei.testingsubsystem.domain.Solution
import cz.upce.fei.testingsubsystem.domain.TestResult
import cz.upce.fei.testingsubsystem.repository.TestResultRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.Closeable

@Service
class GradleService(
    private val testResultRepository: TestResultRepository,
    private val dockerClient: DockerClient,
    private val hostConfig: HostConfig,

) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun createGradleImage(buildImageCmd: BuildImageCmd, testResult: TestResult?, log: StringBuffer): String? {
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

    fun runGradleContainer(imageId: String, testResult: TestResult?, log: StringBuffer) {
        val container = dockerClient.createContainerCmd("$imageId:latest")
            .withCmd(DockerService.DockerCommands.GRADLE_TEST)
            .withHostConfig(hostConfig)
            .withStopTimeout(Int.MAX_VALUE)
            .withTty(true)
            .exec()

        val command = dockerClient.startContainerCmd(container.id)
            .exec()
        logger.debug(command.toString())

        dockerClient.waitContainerCmd(container.id).exec(object : WaitContainerResultCallback(){})
            .awaitCompletion()

        dockerClient.logContainerCmd(container.id)
            .withStdOut(true)
            .withStdErr(true)
            .withFollowStream(true)
            .exec(object : ResultCallback.Adapter<Frame>() {
                override fun onNext(frame: Frame) {
                    logger.info(String(frame.payload))
                }
            })
            .awaitCompletion()

        dockerClient.close()
    }

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
}