package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.Challenge
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface ChallengeRepository : PagingAndSortingRepository<Challenge, Long> {
    fun findByIdEquals(id: Long) : Challenge?
}