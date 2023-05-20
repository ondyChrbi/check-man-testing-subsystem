package cz.upce.fei.testingsubsystem.service.solution

import cz.upce.fei.testingsubsystem.domain.user.AppUser
import cz.upce.fei.testingsubsystem.domain.course.Challenge
import cz.upce.fei.testingsubsystem.domain.testing.Solution
import cz.upce.fei.testingsubsystem.repository.ChallengeRepository
import cz.upce.fei.testingsubsystem.repository.SolutionRepository
import cz.upce.fei.testingsubsystem.service.RecordNotFoundException
import cz.upce.fei.testingsubsystem.service.ResourceNotFoundException
import cz.upce.fei.testingsubsystem.service.TestConfigurationService
import cz.upce.fei.testingsubsystem.service.solution.exception.CannotUploadSolutionToChallengeException
import cz.upce.fei.testingsubsystem.service.solution.exception.NoTestConfigurationSetException
import cz.upce.fei.testingsubsystem.service.testing.TestResultService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime

@Service
class SolutionService(
    private val solutionRepository: SolutionRepository,
    private val challengeRepository: ChallengeRepository,
    private val testConfigurationService: TestConfigurationService,
    private val testResultService: TestResultService,
) {
    @Transactional
    fun findById(id: Long): Solution {
        val solution = solutionRepository.findById(id)

        if (solution.isEmpty) {
            throw ResourceNotFoundException(Solution::class.java, id)
        }

        return solution.get()
    }

    @Throws(RecordNotFoundException::class, NoTestConfigurationSetException::class)
    @Transactional
    fun add(challengeId: Long, appUser: AppUser, file: MultipartFile): Solution {
        val challenge = challengeRepository.findByIdEquals(challengeId)
            ?: throw RecordNotFoundException(Challenge::class.java, challengeId)
        checkChallenge(challenge)

        if (challenge.testConfiguration != null) {
            testResultService.checkExistWaiting(challenge, appUser)
        }

        return solutionRepository.save(Solution(path = save(file).toString(), user = appUser, challenge = challenge))
    }

    fun downloadById(id: Long): Path {
        val solution = solutionRepository.findById(id)

        if (solution.isEmpty) {
            throw RecordNotFoundException(Solution::class.java, id)
        }

        val path = Paths.get(solution.get().path)

        if (Files.notExists(path)) {
            throw RecordNotFoundException(Solution::class.java, id)
        }

        return path
    }

    fun checkAuthor(solution: Solution, appUser: AppUser): Boolean {
        return (solution.user != appUser)
    }

    private fun checkChallenge(challenge: Challenge) {
        if (!challenge.active) {
            throw CannotUploadSolutionToChallengeException(challenge)
        }

        if (challenge.startDate != null && LocalDateTime.now().isBefore(challenge.startDate)) {
            throw CannotUploadSolutionToChallengeException(challenge, "Cannot upload solution to challenge before start date")
        }

        if (challenge.deadlineDate != null && LocalDateTime.now().isAfter(challenge.deadlineDate)) {
            throw CannotUploadSolutionToChallengeException(challenge, "Cannot upload solution to challenge after deadline date")
        }
    }

    private fun save(file: MultipartFile): Path {
        val type = TestConfigurationService.Type.SOLUTION
        return testConfigurationService.add(file, type)
    }
}