package cz.upce.fei.testingsubsystem.service

import cz.upce.fei.testingsubsystem.domain.AppUser
import cz.upce.fei.testingsubsystem.domain.Challenge
import cz.upce.fei.testingsubsystem.domain.Solution
import cz.upce.fei.testingsubsystem.repository.AppUserRepository
import cz.upce.fei.testingsubsystem.repository.ChallengeRepository
import cz.upce.fei.testingsubsystem.repository.SolutionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class SolutionService(
    private val solutionRepository: SolutionRepository,
    private val challengeRepository: ChallengeRepository,
    private val appUserRepository: AppUserRepository,
    private val templateService: TemplateService
) {
    private var contextPath : String = ""

    @Throws(RecordNotFoundException::class)
    @Transactional
    fun add(challengeId: Long, appUser: AppUser, file: MultipartFile): Solution {
        val challenge = challengeRepository.findByIdEquals(challengeId)
            ?: throw RecordNotFoundException(Challenge::class.java, challengeId)
        val path = save(file)

        return solutionRepository.save(Solution(path = path, user = appUser, challenge = challenge))
    }

    private fun save(file: MultipartFile): String {
        val type = TemplateService.Type.SOLUTION
        val fileName = templateService.add(file, type).fileName

        return "${contextPath}/${type.toString().lowercase()}/${fileName}"
    }
}