package cz.upce.fei.testingsubsystem.component.testing

import cz.upce.fei.testingsubsystem.domain.testing.Solution
import cz.upce.fei.testingsubsystem.repository.SolutionRepository
import cz.upce.fei.testingsubsystem.service.testing.TestingService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class TestJob(
    private val testingService: TestingService,
    private val solutionRepository: SolutionRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    fun runTesting() {
        logger.info("Checking new solutions to test")
        val solutionToTest = solutionRepository.findFirsToToTest()

        if (solutionToTest != null) {
            logger.debug("Prepare to test $solutionToTest solution...")

            val testResult = testingService.initNewTestResult(solutionToTest)
            val log = StringBuffer("")

            try {
                testingService.test(solutionToTest, testResult, log)
                testingService.updateStatus(testResult, Solution.TestStatus.FINISHED)
            } catch (e: Exception) {
                logger.error(e.message)
                testingService.updateStatus(testResult, Solution.TestStatus.ERROR)
            } finally {
                testingService.updateLog(testResult, log.toString())
            }
        }
    }
}