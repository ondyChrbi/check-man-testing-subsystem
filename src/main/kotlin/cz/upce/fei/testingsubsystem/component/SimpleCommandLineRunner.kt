package cz.upce.fei.testingsubsystem.component

import cz.upce.fei.testingsubsystem.service.testing.TestingService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class SimpleCommandLineRunner(private val testingService: TestingService) : CommandLineRunner {
    override fun run(vararg args: String?) {

    }
}