package cz.upce.fei.testingsubsystem.service

import cz.upce.fei.testingsubsystem.domain.Challenge
import cz.upce.fei.testingsubsystem.repository.ChallengeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChallengeService(private val challengeRepository: ChallengeRepository) {
    @Transactional
    fun findById(id: Long): Challenge? {
        return challengeRepository.findByIdEquals(id)
    }
}
