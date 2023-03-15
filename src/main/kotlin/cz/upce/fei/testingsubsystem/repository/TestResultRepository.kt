package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.TestResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TestResultRepository : JpaRepository<TestResult, Long> {
}