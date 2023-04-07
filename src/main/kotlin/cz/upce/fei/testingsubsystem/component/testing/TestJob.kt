package cz.upce.fei.testingsubsystem.component.testing

import cz.upce.fei.testingsubsystem.component.testing.module.gradle.GradleModule
import cz.upce.fei.testingsubsystem.domain.testing.Solution
import cz.upce.fei.testingsubsystem.repository.SolutionRepository
import cz.upce.fei.testingsubsystem.service.testing.TestingService
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class TestJob(
    private val testingService: TestingService,
    private val solutionRepository: SolutionRepository,
    private val applicationContext: ApplicationContext
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    fun runTesting() {
        logger.info("Checking new solutions to test")
        val solutionToTest = solutionRepository.findFirsToToTest(Solution.TestStatus.WAITING_TO_TEST.id)

        if (solutionToTest != null) {
            logger.debug("Prepare to test $solutionToTest solution...")

            val testResult = testingService.initNewTestResult(solutionToTest)
            val testModule = applicationContext.getBean(DEFAULT_TEST_MODULE)

            try {
                testingService.test(solutionToTest, testModule, testResult)
                testingService.updateStatus(testResult, Solution.TestStatus.FINISHED)
            } catch (e: Exception) {
                logger.error(e.message)
                testingService.updateStatus(testResult, Solution.TestStatus.ERROR)
            }
        }
    }

    private companion object {
        val DEFAULT_TEST_MODULE = GradleModule::class.java
    }
}