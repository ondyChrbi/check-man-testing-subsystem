package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.Solution
import cz.upce.fei.testingsubsystem.domain.TestConfiguration
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
}