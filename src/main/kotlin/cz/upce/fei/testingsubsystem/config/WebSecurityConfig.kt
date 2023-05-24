package cz.upce.fei.testingsubsystem.config

import cz.upce.fei.testingsubsystem.service.authentication.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*

@Configuration
@EnableWebSecurity
class WebSecurityConfig(private val authenticationFilter: JwtAuthenticationFilter) {
    @Value("\${spring.security.permit_paths}")
    private var permitPaths : Array<String> = arrayOf()

    @Value("\${check-man.security.origins}")
    private var allowedOrigins : Array<String> = arrayOf()

    @Value("\${check-man.security.headers}")
    private var allowedHeaders : String = ""

    @Value("\${check-man.security.methods}")
    private var allowedMethods : String = ""

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .cors().and()
            .csrf().disable()
            .authorizeHttpRequests {
                it.requestMatchers(*permitPaths).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = allowedOrigins.toList()
        configuration.allowedHeaders = listOf(allowedHeaders)
        configuration.allowedMethods = listOf(allowedMethods)
        configuration.allowCredentials = true
        configuration.maxAge = 3600L
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}