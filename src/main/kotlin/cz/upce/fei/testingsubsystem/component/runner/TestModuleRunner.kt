package cz.upce.fei.testingsubsystem.component.runner

import cz.upce.fei.testingsubsystem.service.testing.TestingModuleService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class TestModuleRunner(
    private val testingModuleService: TestingModuleService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun commandLineRunner(): CommandLineRunner {
        return CommandLineRunner {
            logger.info("Testing modules:")
            val modules = testingModuleService.findTestingModules()

            modules.forEach {
                testingModuleService.validateTestingModule(it)
                testingModuleService.logTestingModule(it)
            }
        }
    }
}