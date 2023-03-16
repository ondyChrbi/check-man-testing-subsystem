package cz.upce.fei.testingsubsystem.component.testing

import cz.upce.fei.testingsubsystem.domain.TestResult
import cz.upce.fei.testingsubsystem.service.testing.IllegalResultPathException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isDirectory

interface ModuleTest {
    fun test(dockerFile: Path, testResult: TestResult? = null, resultPath : Path = Paths.get(DEFAULT_RESULT_PATH)) {
        validateResultPath(resultPath)
    }

    private companion object {
        const val DEFAULT_RESULT_PATH = "result"

        fun validateResultPath(resultPath: Path) {
            if (!Files.exists(resultPath)) { Files.createDirectories(resultPath) }

            if (!resultPath.isDirectory()) { throw IllegalResultPathException(resultPath) }

            Files.newDirectoryStream(resultPath).use { directoryStream ->
                if (directoryStream.iterator().hasNext()) { throw IllegalResultPathException(resultPath) }
            }
        }
    }
}
