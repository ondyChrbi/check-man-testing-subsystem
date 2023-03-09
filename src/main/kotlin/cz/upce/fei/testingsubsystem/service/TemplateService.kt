package cz.upce.fei.testingsubsystem.service

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
    private lateinit var filesLocation : String

    @PostConstruct
    fun init() {
        Files.createDirectories(Paths.get(filesLocation))
    }

    fun add(file: MultipartFile): String {
        checkFileValidity(file)

        val locationToSave = prepareToSave(file)
        Files.copy(file.inputStream, locationToSave, StandardCopyOption.REPLACE_EXISTING)
        logger.info("Template saved to location ${locationToSave.toUri()}")

        return locationToSave.fileName.toString()
    }

    private fun prepareToSave(file: MultipartFile) : Path {
        val originalFileName = file.originalFilename!!
        val extension = originalFileName.substring(originalFileName.lastIndexOf("."))

        val baseFolder = Paths.get(filesLocation)
        val newFileName = Paths.get("${UUID.randomUUID()}${extension}")

        return baseFolder.resolve(newFileName)
    }

    private fun checkFileValidity(file: MultipartFile) {
        if (!SUPPORTED_EXTENSION.any { file.originalFilename!!.endsWith(it) }) {
            throw NotSupportedFileExtensionException(file.originalFilename!!, SUPPORTED_EXTENSION)
        }
    }

    private companion object {
        val SUPPORTED_EXTENSION = setOf(".zip")
    }
}