package cz.upce.fei.testingsubsystem.service.testing.exception

import java.nio.file.Path

class TemplateFileNotExistException(templatePath: Path) : Exception("""
    Template file not found $templatePath.
""".trimIndent())