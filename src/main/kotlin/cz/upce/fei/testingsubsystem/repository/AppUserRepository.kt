package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.user.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppUserRepository : JpaRepository<AppUser, Long> {
    fun findByStagIdEquals(stagId: String) : AppUser?
}