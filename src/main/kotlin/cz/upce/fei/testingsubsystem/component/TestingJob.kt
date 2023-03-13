package cz.upce.fei.testingsubsystem.component

import cz.upce.fei.testingsubsystem.domain.Solution
import cz.upce.fei.testingsubsystem.repository.SolutionRepository
import cz.upce.fei.testingsubsystem.service.testing.TestingService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class TestingJob(
    private val solutionRepository: SolutionRepository,
    private val testingService: TestingService
) {
    private val logger = LoggerFactory.getLogger(TestingJob::class.java)


    @Scheduled(fixedDelay = 6000)
    fun runTesting() {
        val toTest = solutionRepository.findFirstByTestStatusIdEqualsOrderByUploadDateAsc(Solution.TestStatus.WAITING_TO_TEST.id)

        if (toTest != null) {
            logger.debug("Prepare to test $toTest solution...")
        }
    }
}