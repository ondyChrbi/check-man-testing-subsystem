package cz.upce.fei.testingsubsystem.service.authentication

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtUtils {
    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    fun parseJwtToken(authToken: String): Jws<Claims> {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
            .build()
            .parseClaimsJws(authToken)
    }
}