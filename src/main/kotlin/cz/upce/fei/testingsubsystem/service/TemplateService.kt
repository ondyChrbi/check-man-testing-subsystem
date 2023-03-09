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
import java.util.zip.ZipFile


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

        val locationToSave = createLocationToSave(file)
        Files.copy(file.inputStream, locationToSave, StandardCopyOption.REPLACE_EXISTING)
        logger.info("Template saved to location ${locationToSave.toUri()}")

        checkGradleProjectValidity(locationToSave)

        return locationToSave.fileName.toString()
    }

    private fun createLocationToSave(file: MultipartFile) : Path {
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

    private fun checkGradleProjectValidity(file: Path) {
        if (isNotValidGradleProject(file)) {
            throw NotValidGradleProjectException()
        }
    }

    private fun isValidGradleProject(file: Path): Boolean {
        var containsBuildFile = false
        var containsGradleWrapper = false
        var containsSrcDir = false

        ZipFile(file.toFile()).use { zipFile ->
            val entries = zipFile.entries()

            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()

                if (!entry.isDirectory) {
                    if (GRADLE_BUILD_REQUIRED.contains(entry.name) || GRADLE_BUILD_APP_REQUIRED.contains(entry.name))
                        containsBuildFile = true
                    else if (GRADLE_WRAPPER_REQUIRED.contains(entry.name) || entry.name.startsWith(GRADLE_WRAPPER_FOLDER))
                        containsGradleWrapper = true
                    else if (entry.name.startsWith(GRADLE_SRC_FOLDER) || entry.name.startsWith(GRADLE_SRC_APP_FOLDER))
                        containsSrcDir = true
                }
            }

        }

        return containsBuildFile && containsGradleWrapper && containsSrcDir
    }

    private fun isNotValidGradleProject(file: Path): Boolean {
        return !isValidGradleProject(file)
    }

    private companion object {
        val SUPPORTED_EXTENSION = setOf(".zip")
        val GRADLE_BUILD_REQUIRED = setOf("build.gradle", "build.gradle.kts")
        val GRADLE_BUILD_APP_REQUIRED = setOf("app/build.gradle", "app/build.gradle.kts")
        val GRADLE_WRAPPER_REQUIRED = setOf("gradlew", "gradlew.bat")
        const val GRADLE_WRAPPER_FOLDER = "gradle/wrapper/"
        const val GRADLE_SRC_APP_FOLDER = "app/src/"
        const val GRADLE_SRC_FOLDER = "src/"
    }
}