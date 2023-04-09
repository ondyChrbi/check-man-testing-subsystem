package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.course.Challenge
import cz.upce.fei.testingsubsystem.domain.testing.Solution
import cz.upce.fei.testingsubsystem.domain.testing.TestResult
import cz.upce.fei.testingsubsystem.domain.user.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TestResultRepository : JpaRepository<TestResult, Long> {
    @Query(
        """
        select distinct tr from TestResult tr
        
        where tr.solution.challenge = :challenge
        and tr.solution.user = :appUser
        and tr.testStatusId = :testStatusId
    """
    )
    fun findByAppUserChallengeAndStatus(
        appUser: AppUser,
        challenge: Challenge,
        testStatusId: Long = Solution.TestStatus.WAITING_TO_TEST.id
    ): List<TestResult>
}