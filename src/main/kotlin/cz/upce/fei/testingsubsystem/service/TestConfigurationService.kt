package cz.upce.fei.testingsubsystem.service

import cz.upce.fei.testingsubsystem.domain.testing.TestConfiguration
import cz.upce.fei.testingsubsystem.dto.TestConfigurationDtoV1
import cz.upce.fei.testingsubsystem.lib.GradleValidator
import cz.upce.fei.testingsubsystem.repository.TestConfigurationRepository
import cz.upce.fei.testingsubsystem.service.solution.AuthenticationService
import cz.upce.fei.testingsubsystem.service.testing.TestingModuleService
import cz.upce.fei.testingsubsystem.service.testing.exception.ChallengeAlreadyHasConfigurationException
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
@Transactional
class TestConfigurationService(
    private val authenticationService: AuthenticationService,
    private val testingModuleService: TestingModuleService,
    private val testConfigurationRepository: TestConfigurationRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${check-man.template.location}")
    private lateinit var templateFilesLocation : String

    @Value("\${check-man.solution.location}")
    private lateinit var solutionFilesLocation : String

    @Value("\${check-man.docker-files.location}")
    private lateinit var dockerFilesLocation : String

    @PostConstruct
    fun init() {
        val templateLocation = Files.createDirectories(Paths.get(templateFilesLocation))
        logger.info("New directory for templates created at: $templateLocation")

        val solutionLocation = Files.createDirectories(Paths.get(solutionFilesLocation))
        logger.info("New directory for templates created at: $solutionLocation")

        val dockerFilesLocation = Files.createDirectories(Paths.get(dockerFilesLocation))
        logger.info("New directory for templates created at: $dockerFilesLocation")
    }

    fun add(testConfiguration: TestConfiguration, challengeId: Long): TestConfiguration {
        testingModuleService.validateTestingModule(testConfiguration.testModuleClass)

        val challenge = authenticationService.findById(challengeId)

        if (testConfigurationRepository.existsByChallengeEquals(challenge)) {
            throw ChallengeAlreadyHasConfigurationException(challenge)
        }

        testConfiguration.challenge = challenge
        return testConfigurationRepository.save(testConfiguration)
    }

    fun add(file: MultipartFile, challengeId: Long): TestConfiguration {
        val challenge = authenticationService.findById(challengeId)
        val path = add(file)

        return testConfigurationRepository.save(
            TestConfiguration(
                templatePath = path.toString(),
                challenge = challenge
            )
        )
    }

    fun add(file: MultipartFile, type: Type = Type.TEMPLATE): Path {
        GradleValidator.checkFileValidity(file)

        val locationToSave = createLocationToSave(file, type)
        Files.copy(file.inputStream, locationToSave, StandardCopyOption.REPLACE_EXISTING)
        logger.info("$type saved to location ${locationToSave.toUri()}")

        GradleValidator.checkGradleProjectValidity(locationToSave)

        return locationToSave
    }

    fun addTemplate(file: MultipartFile, testConfigurationId: Long): TestConfiguration {
        val configuration = testConfigurationRepository.findById(testConfigurationId)
            .orElseThrow { RecordNotFoundException(TestConfiguration::class.java, testConfigurationId) }

        configuration.templatePath = add(file, Type.TEMPLATE).toString()
        return testConfigurationRepository.save(configuration)
    }

    fun addDockerFile(id: Long, file: MultipartFile): TestConfiguration {
        val configurationOptional = testConfigurationRepository.findById(id)

        if (configurationOptional.isEmpty) {
            throw RecordNotFoundException(TestConfiguration::class.java, id)
        }

        val configuration = configurationOptional.get()
        configuration.dockerFilePath = saveDockerFile(file).toString()

        testConfigurationRepository.saveAndFlush(configuration)

        return configuration
    }

    fun findAll(challengeId: Long): Collection<TestConfiguration> {
        val challenge = authenticationService.findById(challengeId)

        return testConfigurationRepository.findAllByChallengeEquals(challenge)
    }

    fun findByChallenge(challengeId: Long): TestConfiguration? {
        val challenge = authenticationService.findById(challengeId)

        return testConfigurationRepository.findFirstByChallengeEquals(challenge)
    }

    private fun saveDockerFile(file: MultipartFile): Path {
        val dockerFileDirectory = Paths.get(dockerFilesLocation)
        val dockerFile = Paths.get("DockerFile-${UUID.randomUUID()}")
        val locationToSave = dockerFileDirectory.resolve(dockerFile)

        Files.copy(file.inputStream, locationToSave, StandardCopyOption.REPLACE_EXISTING)

        return locationToSave
    }

    private fun createLocationToSave(file: MultipartFile, type: Type = Type.TEMPLATE) : Path {
        val originalFileName = file.originalFilename!!
        val extension = originalFileName.substring(originalFileName.lastIndexOf("."))

        val baseFolder = Paths.get(
            if (type == Type.TEMPLATE)
                templateFilesLocation
            else
                solutionFilesLocation
        )
        val newFileName = Paths.get("${UUID.randomUUID()}${extension}")

        return baseFolder.resolve(newFileName)
    }

    fun patch(testConfigurationId: Long, testConfigurationDto: TestConfigurationDtoV1): TestConfiguration {
        val configurationOptional = testConfigurationRepository.findById(testConfigurationId)

        if (configurationOptional.isEmpty) {
            throw ResourceNotFoundException(TestConfiguration::class.java, testConfigurationId)
        }

        return testConfigurationRepository.save(
            configurationOptional.get().patch(testConfigurationDto)
        )
    }

    enum class Type {
        TEMPLATE, SOLUTION
    }
}