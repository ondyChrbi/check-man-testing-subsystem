package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.AppUser
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface AppUserRepository : PagingAndSortingRepository<AppUser, Long> {
    fun findByStagIdEquals(stagId: String) : AppUser?
}