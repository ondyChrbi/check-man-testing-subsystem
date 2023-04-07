package cz.upce.fei.testingsubsystem.service.authentication

import cz.upce.fei.testingsubsystem.domain.user.AppUser
import cz.upce.fei.testingsubsystem.repository.AppUserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserAuthenticationService(
    private val appUserRepository: AppUserRepository
) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val result = appUserRepository.findByStagIdEquals(username)
            ?: throw UsernameNotFoundException("User $username not found or disabled")

        return UserDetailsImpl(result)
    }

    @Transactional
    fun extractAuthenticateUser(authentication: Authentication): AppUser {
        if (authentication is UsernamePasswordAuthenticationToken && authentication.principal is UserDetailsImpl) {
            return appUserRepository.findByStagIdEquals((authentication.principal as UserDetailsImpl).stagId)
                ?: throw WrongSecurityPrincipalsException()
        }

        throw WrongSecurityPrincipalsException()
    }
}