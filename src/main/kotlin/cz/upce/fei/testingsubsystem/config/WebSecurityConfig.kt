package cz.upce.fei.testingsubsystem.config

import cz.upce.fei.testingsubsystem.service.authentication.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class WebSecurityConfig(private val authenticationFilter: JwtAuthenticationFilter) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .authorizeHttpRequests { it.anyRequest().authenticated() }
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }


}