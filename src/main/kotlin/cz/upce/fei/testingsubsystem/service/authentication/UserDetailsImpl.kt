package cz.upce.fei.testingsubsystem.service.authentication

import cz.upce.fei.testingsubsystem.domain.AppUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserDetailsImpl(
    private val id: Long,
    private val stagId: String,
    private val locked: Boolean = false,
    private val expired: Boolean = false,
    private val authorities: MutableCollection<out GrantedAuthority> = mutableListOf()
) : UserDetails {
    constructor(appUser: AppUser) : this(appUser.id!!, appUser.stagId, appUser.disabled,
        authorities = appUser.roles.map { SimpleGrantedAuthority(it.name) }.toMutableList())

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return stagId
    }

    override fun isAccountNonExpired(): Boolean {
        return !expired
    }

    override fun isAccountNonLocked(): Boolean {
        return !locked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return isAccountNonExpired && isAccountNonLocked
    }

    override fun isEnabled(): Boolean {
        return isAccountNonExpired && isAccountNonLocked
    }
}
