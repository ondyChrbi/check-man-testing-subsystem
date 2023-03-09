package cz.upce.fei.testingsubsystem.service

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class NotSupportedFileExtensionException(originalFilename: String, supported: Collection<String>) : Exception("""
    Not supported file extension: ${originalFilename}. Supported are: ${supported.joinToString(",")}
""".trimIndent())
