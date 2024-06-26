package cz.upce.fei.testingsubsystem.service.testing.exception

import java.nio.file.Path

class IllegalResultPathException(resultPath: Path) : Exception("""
    Not valid result path: ${resultPath.toAbsolutePath()}. Must be an a directory.
""".trimIndent())