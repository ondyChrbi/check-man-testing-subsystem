package cz.upce.fei.testingsubsystem.service.solution.exception

import cz.upce.fei.testingsubsystem.domain.course.Challenge
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CannotUploadSolutionToChallengeException(challenge: Challenge, message: String = "Cannot upload solution to this challenge.") : Exception("""
    $message: ${challenge.id}
""".trimIndent())