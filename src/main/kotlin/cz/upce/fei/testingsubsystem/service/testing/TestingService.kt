package cz.upce.fei.testingsubsystem.service.testing

import cz.upce.fei.testingsubsystem.domain.Solution
import cz.upce.fei.testingsubsystem.domain.TestConfiguration
import cz.upce.fei.testingsubsystem.repository.TestConfigurationRepository
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Service
class TestingService(private val testConfigurationRepository: TestConfigurationRepository) {
    private val logger = LoggerFactory.getLogger(TestingService::class.java)

    @Value("\${check-man.playground.location}")
    private lateinit var playgroundFilesLocation : String

    @PostConstruct
    fun init() {
        val playgroundLocation = Files.createDirectories(Paths.get(playgroundFilesLocation))
        logger.info("New directory for playground created at: $playgroundLocation")
    }

    @Transactional
    fun test(solution: Solution) {
        val configuration = testConfigurationRepository.findBySolution(solution)

        val playgroundLocation = preparePlayground(solution, configuration)
    }

    private fun preparePlayground(solution: Solution, configuration: TestConfiguration): Path {
        val testingPlaygroundDir = Paths.get(playgroundFilesLocation).resolve("${UUID.randomUUID()}")
        val location = Files.createDirectories(testingPlaygroundDir)
        logger.debug("Crated new empty playground for solution ${solution.id}: $location")

        copySolutionToPlayground(solution, testingPlaygroundDir)
        copyTemplateToPlayground(configuration, testingPlaygroundDir)

        return testingPlaygroundDir
    }

    private fun copyTemplateToPlayground(configuration: TestConfiguration, testingPlaygroundDir: Path) {
        if (configuration.templatePath == null) { throw TemplateFileNotSetException(configuration) }
        if (configuration.dockerFilePath == null) { throw DockerFileNotSetException(configuration) }

        val templatePath = Paths.get(configuration.templatePath!!)
        val dockerFilePath = Paths.get(configuration.dockerFilePath!!)

        if (Files.notExists(templatePath)) { throw TemplateFileNotExistException(templatePath) }
        if (Files.notExists(dockerFilePath)) { throw DockerFileNotExistException(dockerFilePath) }

        Files.copy(templatePath, testingPlaygroundDir.resolve(templatePath.fileName))
        Files.copy(dockerFilePath, testingPlaygroundDir.resolve(DOCKER_FILE_NAME))
    }

    private fun copySolutionToPlayground(solution: Solution, testingPlaygroundDir: Path): Path {
        val solutionFile = Paths.get(solution.path)

        if (Files.notExists(solutionFile)) {
            throw SolutionFileNotFoundException(solution)
        }

        val location = testingPlaygroundDir.resolve(solutionFile.fileName)
        Files.copy(solutionFile, location)
        logger.debug("File  ${solution.id}: ${solution.path}")

        return location
    }

    private companion object {
        const val DOCKER_FILE_NAME = "DockerFile"
    }
}