package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.Solution
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SolutionRepository : JpaRepository<Solution, Long> {

}
