package cz.upce.fei.testingsubsystem.lib

import cz.upce.fei.testingsubsystem.service.NotSupportedFileExtensionException
import cz.upce.fei.testingsubsystem.service.NotValidGradleProjectException
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.zip.ZipFile

object GradleValidator {
    fun checkFileValidity(file: MultipartFile) {
        if (!SUPPORTED_EXTENSION.any { file.originalFilename!!.endsWith(it) }) {
            throw NotSupportedFileExtensionException(file.originalFilename!!, SUPPORTED_EXTENSION)
        }
    }

    fun checkGradleProjectValidity(file: Path) {
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
                    else if (GRADLE_WRAPPER_REQUIRED.contains(entry.name) || entry.name.startsWith(
                            GRADLE_WRAPPER_FOLDER
                        ))
                        containsGradleWrapper = true
                    else if (entry.name.startsWith(GRADLE_SRC_FOLDER) || entry.name.startsWith(
                            GRADLE_SRC_APP_FOLDER
                        ))
                        containsSrcDir = true
                }
            }

        }

        return containsBuildFile && containsGradleWrapper && containsSrcDir
    }

    private fun isNotValidGradleProject(file: Path): Boolean {
        return !isValidGradleProject(file)
    }

    private val SUPPORTED_EXTENSION = setOf(".zip")
    private val GRADLE_BUILD_REQUIRED = setOf("build.gradle", "build.gradle.kts")
    private val GRADLE_BUILD_APP_REQUIRED = setOf("app/build.gradle", "app/build.gradle.kts")
    private val GRADLE_WRAPPER_REQUIRED = setOf("gradlew", "gradlew.bat")
    private const val GRADLE_WRAPPER_FOLDER = "gradle/wrapper/"
    private const val GRADLE_SRC_APP_FOLDER = "app/src/"
    private const val GRADLE_SRC_FOLDER = "src/"
}