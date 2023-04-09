package cz.upce.fei.testingsubsystem.service.solution

import cz.upce.fei.testingsubsystem.domain.user.AppUser
import cz.upce.fei.testingsubsystem.domain.course.Challenge
import cz.upce.fei.testingsubsystem.domain.testing.Solution
import cz.upce.fei.testingsubsystem.repository.ChallengeRepository
import cz.upce.fei.testingsubsystem.repository.SolutionRepository
import cz.upce.fei.testingsubsystem.service.RecordNotFoundException
import cz.upce.fei.testingsubsystem.service.TestConfigurationService
import cz.upce.fei.testingsubsystem.service.solution.exception.NoTestConfigurationSetException
import cz.upce.fei.testingsubsystem.service.testing.TestResultService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path

@Service
class SolutionService(
    private val solutionRepository: SolutionRepository,
    private val challengeRepository: ChallengeRepository,
    private val testConfigurationService: TestConfigurationService,
    private val testResultService: TestResultService,
) {
    @Throws(RecordNotFoundException::class, NoTestConfigurationSetException::class)
    @Transactional
    fun add(challengeId: Long, appUser: AppUser, file: MultipartFile): Solution {
        val challenge = challengeRepository.findByIdEquals(challengeId)
            ?: throw RecordNotFoundException(Challenge::class.java, challengeId)

        if (challenge.testConfiguration != null) {
            testResultService.checkExistWaiting(challenge, appUser)
        }

        return solutionRepository.save(Solution(path = save(file).toString(), user = appUser, challenge = challenge))
    }

    private fun save(file: MultipartFile): Path {
        val type = TestConfigurationService.Type.SOLUTION
        return testConfigurationService.add(file, type)
    }
}