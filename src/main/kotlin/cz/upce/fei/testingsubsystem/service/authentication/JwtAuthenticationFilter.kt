package cz.upce.fei.testingsubsystem.service.authentication

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter

@Service
class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils,
    private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val claims = jwtUtils.parseJwtToken(request.getHeader("Authorization"))

        val userDetails = userDetailsService.loadUserByUsername(claims.body.subject)

        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }

}
