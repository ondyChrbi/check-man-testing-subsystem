package cz.upce.fei.testingsubsystem.service.solution

import cz.upce.fei.testingsubsystem.domain.AppUser
import cz.upce.fei.testingsubsystem.domain.Challenge
import cz.upce.fei.testingsubsystem.domain.Solution
import cz.upce.fei.testingsubsystem.repository.ChallengeRepository
import cz.upce.fei.testingsubsystem.repository.SolutionRepository
import cz.upce.fei.testingsubsystem.service.RecordNotFoundException
import cz.upce.fei.testingsubsystem.service.TemplateService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path

@Service
class SolutionService(
    private val solutionRepository: SolutionRepository,
    private val challengeRepository: ChallengeRepository,
    private val templateService: TemplateService
) {
    @Throws(RecordNotFoundException::class)
    @Transactional
    fun add(challengeId: Long, appUser: AppUser, file: MultipartFile): Solution {
        val challenge = challengeRepository.findByIdEquals(challengeId)
            ?: throw RecordNotFoundException(Challenge::class.java, challengeId)
        val path = save(file)

        return solutionRepository.save(Solution(path = path.toString(), user = appUser, challenge = challenge))
    }

    private fun save(file: MultipartFile): Path {
        val type = TemplateService.Type.SOLUTION
        return templateService.add(file, type)
    }
}