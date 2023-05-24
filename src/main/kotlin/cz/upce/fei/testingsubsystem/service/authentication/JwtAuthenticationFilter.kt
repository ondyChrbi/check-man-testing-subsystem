package cz.upce.fei.testingsubsystem.service.authentication

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Service
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

@Service
class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils,
    private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {
    @Value("\${spring.security.permit_paths}")
    private var permitPaths : Array<String> = arrayOf()

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

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        permitPaths.forEach {
            if (AntPathMatcher().match(it, request.requestURI)) {
                return true
            }
        }

        return super.shouldNotFilter(request)
    }
}
