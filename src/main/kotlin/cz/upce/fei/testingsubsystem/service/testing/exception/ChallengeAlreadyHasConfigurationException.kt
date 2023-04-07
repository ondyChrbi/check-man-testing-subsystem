package cz.upce.fei.testingsubsystem.service.testing.exception

import cz.upce.fei.testingsubsystem.domain.course.Challenge
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.Exception

@ResponseStatus(HttpStatus.CONFLICT)
class ChallengeAlreadyHasConfigurationException(challenge: Challenge) : Exception("""
    $challenge already has test configuration
""".trimIndent())
