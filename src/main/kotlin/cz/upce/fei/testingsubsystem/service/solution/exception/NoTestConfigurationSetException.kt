package cz.upce.fei.testingsubsystem.service.solution.exception

import cz.upce.fei.testingsubsystem.domain.course.Challenge
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.Exception

@ResponseStatus(HttpStatus.BAD_REQUEST)
class NoTestConfigurationSetException(challenge: Challenge) : Exception("""
    No test configuration set for challenge: $challenge.
""".trimIndent())