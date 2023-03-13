package cz.upce.fei.testingsubsystem.service.testing

import cz.upce.fei.testingsubsystem.domain.Solution
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Service
class TestingService {
    private val logger = LoggerFactory.getLogger(TestingService::class.java)

    @Value("\${check-man.playground.location}")
    private lateinit var playgroundFilesLocation : String

    @PostConstruct
    fun init() {
        val playgroundLocation = Files.createDirectories(Paths.get(playgroundFilesLocation))
        logger.info("New directory for playground created at: $playgroundLocation")
    }

    fun test(solution: Solution) {

    }

    private fun preparePlayground(solution: Solution): Path {
        val testingPlaygroundDir = Paths.get(playgroundFilesLocation).resolve("/${UUID.randomUUID()}")
        val location = Files.createDirectories(testingPlaygroundDir)
        logger.debug("Crated new empty playground for solution ${solution.id}: $location")

        val solutionFile = Paths.get(solution.path)

        if (Files.notExists(solutionFile)) {
            throw SolutionFileNotFoundException(solution)
        }

        Files.copy(solutionFile, testingPlaygroundDir.resolve(solutionFile.fileName))
        logger.debug("File  ${solution.id}: ${solution.path}")

        return testingPlaygroundDir
    }
}