package cz.upce.fei.testingsubsystem.component.testing.module

import cz.upce.fei.testingsubsystem.domain.testing.Feedback
import cz.upce.fei.testingsubsystem.domain.testing.Solution
import cz.upce.fei.testingsubsystem.domain.testing.TestResult
import cz.upce.fei.testingsubsystem.service.testing.exception.IllegalResultPathException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isDirectory

interface TestModule {
    fun test(
        dockerFile: Path,
        resultPath: Path = Paths.get(DEFAULT_PLAYGROUND_PATH),
        testResult: TestResult? = null,
        log: StringBuffer = StringBuffer("")
    ): Path {
        validateResultPath(resultPath)
        return resultPath
    }

    fun resultToFeedbacks(resultPath: Path): Collection<Feedback>

    private companion object {
        const val DEFAULT_PLAYGROUND_PATH = ""

        fun validateResultPath(resultPath: Path) {
            if (!Files.exists(resultPath)) {
                Files.createDirectories(resultPath)
            }

            if (!resultPath.isDirectory()) {
                throw IllegalResultPathException(resultPath)
            }

            Files.newDirectoryStream(resultPath).use { directoryStream ->
                if (directoryStream.iterator().hasNext()) { throw IllegalResultPathException(resultPath)
                }
            }
        }
    }
}