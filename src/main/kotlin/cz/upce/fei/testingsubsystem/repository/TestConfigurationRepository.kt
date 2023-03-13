package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.TestConfiguration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TestConfigurationRepository : JpaRepository<TestConfiguration, Long> {

}