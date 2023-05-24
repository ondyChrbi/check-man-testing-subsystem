package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.course.Challenge
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChallengeRepository : JpaRepository<Challenge, Long> {
    fun findByIdEquals(id: Long) : Challenge?
}