package cz.upce.fei.testingsubsystem.component

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DockerConnectivityChecker() : CommandLineRunner {
    @Transactional
    override fun run(vararg args: String?) {

    }
}