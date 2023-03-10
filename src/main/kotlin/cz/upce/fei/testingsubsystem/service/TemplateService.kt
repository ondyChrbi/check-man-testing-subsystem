package cz.upce.fei.testingsubsystem.service

import cz.upce.fei.testingsubsystem.lib.GradleValidator
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*


@Service
class TemplateService {
    private val logger = LoggerFactory.getLogger(TemplateService::class.java)

    @Value("\${check-man.template.location}")
    private lateinit var templateFilesLocation : String

    @Value("\${check-man.solution.location}")
    private lateinit var solutionFilesLocation : String

    @PostConstruct
    fun init() {
        Files.createDirectories(Paths.get(templateFilesLocation))
        Files.createDirectories(Paths.get(solutionFilesLocation))
    }

    fun add(file: MultipartFile, type: Type = Type.TEMPLATE): String {
        GradleValidator.checkFileValidity(file)

        val locationToSave = createLocationToSave(file)
        Files.copy(file.inputStream, locationToSave, StandardCopyOption.REPLACE_EXISTING)
        logger.info("$type saved to location ${locationToSave.toUri()}")

        GradleValidator.checkGradleProjectValidity(locationToSave)

        return locationToSave.fileName.toString()
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