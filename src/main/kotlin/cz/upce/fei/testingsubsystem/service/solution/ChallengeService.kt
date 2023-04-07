package cz.upce.fei.testingsubsystem.service.solution

import cz.upce.fei.testingsubsystem.domain.course.Challenge
import cz.upce.fei.testingsubsystem.repository.ChallengeRepository
import cz.upce.fei.testingsubsystem.service.RecordNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChallengeService(private val challengeRepository: ChallengeRepository) {
    @Transactional
    fun findById(id: Long): Challenge {
        return challengeRepository.findByIdEquals(id) ?:
            throw RecordNotFoundException(Challenge::class.java, id)
    }
}