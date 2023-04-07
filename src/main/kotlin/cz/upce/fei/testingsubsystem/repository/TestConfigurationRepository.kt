package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.course.Challenge
import cz.upce.fei.testingsubsystem.domain.testing.Solution
import cz.upce.fei.testingsubsystem.domain.testing.TestConfiguration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
@Repository
interface TestConfigurationRepository : JpaRepository<TestConfiguration, Long> {
    @Query("""
        select tc
        FROM TestConfiguration tc
        INNER JOIN Challenge c
        INNER JOIN Solution s
        where s = :solution
    """)
    fun findBySolution(solution: Solution) : TestConfiguration

    fun findAllByChallengeEquals(challenge: Challenge) : List<TestConfiguration>

    fun findFirstByChallengeEquals(challenge: Challenge) : TestConfiguration?

    fun existsByChallengeEquals(challenge: Challenge) : Boolean
}