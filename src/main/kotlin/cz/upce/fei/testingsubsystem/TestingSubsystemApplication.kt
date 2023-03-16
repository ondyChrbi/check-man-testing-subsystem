package cz.upce.fei.testingsubsystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TestingSubsystemApplication

fun main(args: Array<String>) {
	runApplication<TestingSubsystemApplication>(*args)
}
