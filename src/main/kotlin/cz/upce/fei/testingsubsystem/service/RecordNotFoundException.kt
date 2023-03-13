package cz.upce.fei.testingsubsystem.service

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class RecordNotFoundException(clazz: Class<*>, challengeId: Any) : Exception("""
    Record ${clazz.name} with id $challengeId not found
""".trimIndent())
