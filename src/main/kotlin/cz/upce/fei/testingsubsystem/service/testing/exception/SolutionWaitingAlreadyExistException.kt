package cz.upce.fei.testingsubsystem.service.testing.exception

import cz.upce.fei.testingsubsystem.domain.course.Challenge
import cz.upce.fei.testingsubsystem.domain.user.AppUser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.Exception

@ResponseStatus(HttpStatus.CONFLICT)
class SolutionWaitingAlreadyExistException(challenge: Challenge, appUser: AppUser) : Exception("""
    Solution waiting to test for challenge ${challenge.id} and user ${appUser.id} already exist.
""".trimIndent())
