package cz.upce.fei.testingsubsystem.service.testing

import cz.upce.fei.testingsubsystem.domain.Solution
import cz.upce.fei.testingsubsystem.domain.TestConfiguration
import cz.upce.fei.testingsubsystem.domain.TestResult
import cz.upce.fei.testingsubsystem.repository.TestConfigurationRepository
import cz.upce.fei.testingsubsystem.repository.TestResultRepository
import cz.upce.fei.testingsubsystem.component.testing.TestModule
import cz.upce.fei.testingsubsystem.domain.Review
import cz.upce.fei.testingsubsystem.service.solution.ReviewService
import jakarta.annotation.PostConstruct
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.apache.commons.compress.utils.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Service
class TestingService(
    private val reviewService: ReviewService,
    private val testConfigurationRepository: TestConfigurationRepository,
    private val testResultRepository: TestResultRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${check-man.playground.location}")
    private lateinit var playgroundFilesLocation : String

    @PostConstruct
    fun init() {
        val playgroundLocation = Files.createDirectories(Paths.get(playgroundFilesLocation))
        logger.info("New directory for playground created at: $playgroundLocation")
    }

    @Transactional(noRollbackFor = [Throwable::class])
    fun test(solution: Solution, testModule: TestModule, testResult: TestResult = initNewTestResult(solution)) {
        val configuration = testConfigurationRepository.findBySolution(solution)
        val playgroundLocation = preparePlayground(solution, configuration)

        val resultLocation = playgroundLocation.resolve(Paths.get(RESULT_DIR_NAME))

        updateStatus(testResult, Solution.TestStatus.RUNNING)
        testModule.test(playgroundLocation.resolve(Paths.get(DOCKER_FILE_NAME)), testResult, resultLocation)
        logger.info("Testing finished for $solution")

        val review = saveFeedbacks(resultLocation, solution, testModule)
        logger.info("Feedbacks from automating testing $solution were saved to $review")
    }

    @Transactional
    fun initNewTestResult(solution: Solution): TestResult {
        return testResultRepository.save(TestResult(solution = solution))
    }

    @Transactional(noRollbackFor = [Exception::class])
    fun updateStatus(testResult: TestResult, status: Solution.TestStatus = Solution.TestStatus.WAITING_TO_TEST): TestResult {
        testResult.testStatusId = status.id
        return testResultRepository.save(testResult)
    }

    @Transactional
    protected fun saveFeedbacks(resultLocation: Path, solution: Solution, testModule: TestModule): Review {
        val review = reviewService.createReviewIfNotExist(solution)
        val feedbacks = testModule.resultToFeedbacks(resultLocation, solution)

        return reviewService.assignFeedbacks(review, feedbacks)
    }

    private fun preparePlayground(solution: Solution, configuration: TestConfiguration): Path {
        val testingPlaygroundDir = Paths.get(playgroundFilesLocation).resolve("${UUID.randomUUID()}")
        val location = Files.createDirectories(testingPlaygroundDir)
        logger.debug("Crated new empty playground for solution ${solution.id}: $location")

        copySolutionToPlayground(solution, testingPlaygroundDir)
        copyTemplateToPlayground(configuration, testingPlaygroundDir)

        return testingPlaygroundDir
    }

    private fun copyTemplateToPlayground(configuration: TestConfiguration, testingPlaygroundDir: Path): Path {
        if (configuration.templatePath == null) { throw TemplateFileNotSetException(configuration) }
        if (configuration.dockerFilePath == null) { throw DockerFileNotSetException(configuration) }

        val templatePath = Paths.get(configuration.templatePath!!)
        val dockerFilePath = Paths.get(configuration.dockerFilePath!!)

        if (Files.notExists(templatePath)) { throw TemplateFileNotExistException(templatePath) }
        if (Files.notExists(dockerFilePath)) { throw DockerFileNotExistException(dockerFilePath) }

        val templateZipLocation = Files.copy(templatePath, testingPlaygroundDir.resolve(templatePath.fileName))
        val unzipTemplateLocation = unzipFile(templateZipLocation, testingPlaygroundDir.resolve(Paths.get(TEMPLATE_DIR_NAME)))
        logger.debug("Zip $templateZipLocation unarchived to location $unzipTemplateLocation")

        Files.copy(dockerFilePath, testingPlaygroundDir.resolve(DOCKER_FILE_NAME))

        return testingPlaygroundDir
    }

    private fun unzipFile(zipFilePath: Path, destDirPath: Path, deleteSource: Boolean = true): Path {
        val zipFile = zipFilePath.toFile()
        val destDir = destDirPath.toFile()

        if (!zipFile.exists()) {
            throw IllegalArgumentException("Zip file not found: $zipFilePath")
        }

        if (!destDir.exists()) {
            Files.createDirectories(destDirPath)
        }

        val zipInputStream = ZipArchiveInputStream(FileInputStream(zipFile))
        var entry = zipInputStream.nextEntry

        while (entry != null) {
            val entryFile = File(destDir, entry.name)

            if (entry.isDirectory) {
                Files.createDirectories(entryFile.toPath())
            } else {
                val entryDir = entryFile.parentFile
                if (!entryDir.exists()) {
                    Files.createDirectories(entryDir.toPath())
                }
                val entryOutputStream = FileOutputStream(entryFile)
                IOUtils.copy(zipInputStream, entryOutputStream)
                entryOutputStream.close()
            }

            entry = zipInputStream.nextEntry
        }

        zipInputStream.close()

        if (deleteSource) {
            Files.deleteIfExists(zipFilePath)
        }

        return destDirPath
    }

    private fun copySolutionToPlayground(solution: Solution, testingPlaygroundDir: Path): Path {
        val solutionFile = Paths.get(solution.path)

        if (Files.notExists(solutionFile)) {
            throw SolutionFileNotFoundException(solution)
        }

        val location = testingPlaygroundDir.resolve(solutionFile.fileName)
        val zipLocation = Files.copy(solutionFile, location)
        logger.debug("File  ${solution.id}: ${solution.path}")

        val unzipLocation = unzipFile(zipLocation, testingPlaygroundDir.resolve(Paths.get(SOLUTION_DIR_NAME)))
        logger.debug("Zip $zipLocation unarchived to location $unzipLocation")

        return location
    }

    private companion object {
        const val DOCKER_FILE_NAME = "Dockerfile"
        const val SOLUTION_DIR_NAME = "solution"
        const val TEMPLATE_DIR_NAME = "template"
        const val RESULT_DIR_NAME = "result"
    }
}