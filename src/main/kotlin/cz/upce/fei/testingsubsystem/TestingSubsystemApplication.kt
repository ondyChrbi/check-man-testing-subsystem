package cz.upce.fei.testingsubsystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestingSubsystemApplication

fun main(args: Array<String>) {
	runApplication<TestingSubsystemApplication>(*args)
}
