package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.testing.Solution
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SolutionRepository : JpaRepository<Solution, Long> {
    @Query("""
        select distinct s
        from Solution as s
        left outer join TestResult as tr
        where s.testResults is empty
        order by s.uploadDate asc
        limit 1
    """)
    fun findFirsToToTest(testStatusId: Long) : Solution?
}
