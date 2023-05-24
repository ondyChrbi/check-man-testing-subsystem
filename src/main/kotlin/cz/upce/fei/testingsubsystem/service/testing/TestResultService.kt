package cz.upce.fei.testingsubsystem.service.testing

import cz.upce.fei.testingsubsystem.domain.course.Challenge
import cz.upce.fei.testingsubsystem.domain.user.AppUser
import cz.upce.fei.testingsubsystem.repository.TestResultRepository
import cz.upce.fei.testingsubsystem.service.testing.exception.SolutionWaitingAlreadyExistException
import org.springframework.stereotype.Service

@Service
class TestResultService(
    private val testResultRepository: TestResultRepository
) {
    fun checkExistWaiting(challenge: Challenge, appUser: AppUser) {
        val testResults = testResultRepository.findByAppUserChallengeAndStatus(appUser, challenge)

        if (testResults.isNotEmpty()) {
            throw SolutionWaitingAlreadyExistException(challenge, appUser)
        }
    }
}