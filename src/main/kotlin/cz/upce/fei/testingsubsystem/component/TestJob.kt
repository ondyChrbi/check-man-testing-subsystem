package cz.upce.fei.testingsubsystem.component

import cz.upce.fei.testingsubsystem.component.testing.GradleModule
import cz.upce.fei.testingsubsystem.domain.Solution
import cz.upce.fei.testingsubsystem.repository.SolutionRepository
import cz.upce.fei.testingsubsystem.service.testing.TestingService
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
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
        val solutionToTest = solutionRepository.findFirstByTestStatusIdEqualsOrderByUploadDateAsc(Solution.TestStatus.WAITING_TO_TEST.id)

        if (solutionToTest != null) {
            logger.debug("Prepare to test $solutionToTest solution...")

            try {
                updateStatus(solutionToTest, Solution.TestStatus.RUNNING)

                val testModule = applicationContext.getBean(DEFAULT_TEST_MODULE)
                testingService.test(solutionToTest, testModule)

                updateStatus(solutionToTest, Solution.TestStatus.FINISHED)
            } catch (e: Throwable) {
                logger.error(e.message)
                updateStatus(solutionToTest, Solution.TestStatus.ERROR)
            }
        }
    }

    @Transactional
    protected fun updateStatus(solution: Solution, status: Solution.TestStatus) {
        solution.testStatusId = status.id
        solutionRepository.saveAndFlush(solution)
    }

    private companion object {
        val DEFAULT_TEST_MODULE = GradleModule::class.java
    }
}