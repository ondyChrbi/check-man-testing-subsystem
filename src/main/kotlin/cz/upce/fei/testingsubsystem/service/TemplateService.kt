package cz.upce.fei.testingsubsystem.service

import cz.upce.fei.testingsubsystem.domain.Challenge
import cz.upce.fei.testingsubsystem.domain.TestConfiguration
import cz.upce.fei.testingsubsystem.lib.GradleValidator
import cz.upce.fei.testingsubsystem.repository.TestConfigurationRepository
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
class TemplateService(
    private val challengeService: ChallengeService,
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

    @Transactional
    fun add(file: MultipartFile, challengeId: Long): TestConfiguration {
        val challenge = challengeService.findById(challengeId)
            ?: throw RecordNotFoundException(Challenge::class.java, challengeId)
        val path = add(file)

        return testConfigurationRepository.save(TestConfiguration(
            templatePath = path.toString(),
            challenge = challenge
        ))
    }

    fun add(file: MultipartFile, type: Type = Type.TEMPLATE): Path {
        GradleValidator.checkFileValidity(file)

        val locationToSave = createLocationToSave(file, type)
        Files.copy(file.inputStream, locationToSave, StandardCopyOption.REPLACE_EXISTING)
        logger.info("$type saved to location ${locationToSave.toUri()}")

        GradleValidator.checkGradleProjectValidity(locationToSave)

        return locationToSave
    }

    @Transactional
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

    enum class Type {
        TEMPLATE, SOLUTION
    }
}