package cz.upce.fei.testingsubsystem.component.docker

import cz.upce.fei.testingsubsystem.service.testing.docker.DockerNotAvailableException
import cz.upce.fei.testingsubsystem.service.testing.docker.DockerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DockerConnectivity(private val dockerService: DockerService) : CommandLineRunner {
    val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String?) {
        if (dockerService.isNotAvailable()) {
            val ex = DockerNotAvailableException()
            log.error(ex.message)

            throw ex
        }

        log.info("Docker detected on this machine.")
    }
}