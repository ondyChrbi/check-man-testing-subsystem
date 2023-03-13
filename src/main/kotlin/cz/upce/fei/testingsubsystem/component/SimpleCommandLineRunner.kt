package cz.upce.fei.testingsubsystem.component

import cz.upce.fei.testingsubsystem.repository.SolutionRepository
import cz.upce.fei.testingsubsystem.service.testing.TestingService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SimpleCommandLineRunner(
    private val testingService: TestingService,
    private val solutionRepository: SolutionRepository
) : CommandLineRunner {
    @Transactional
    override fun run(vararg args: String?) {
        val record = solutionRepository.findById(3L)

        testingService.test(record.get())
    }
}