package cz.upce.fei.testingsubsystem.service.course

import cz.upce.fei.testingsubsystem.domain.course.Challenge
import cz.upce.fei.testingsubsystem.repository.ChallengeRepository
import cz.upce.fei.testingsubsystem.service.ResourceNotFoundException
import org.springframework.stereotype.Service

@Service
class ChallengeService(
    private val challengeRepository: ChallengeRepository
) {
    fun findById(id: Long): Challenge {
        val challenge = challengeRepository.findById(id)

        if (challenge.isEmpty) {
            throw ResourceNotFoundException(Challenge::class.java, id)
        }

        return challenge.get()
    }
}