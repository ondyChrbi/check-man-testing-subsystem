package cz.upce.fei.testingsubsystem.controller

import cz.upce.fei.testingsubsystem.doc.SolutionEndpointV1
import cz.upce.fei.testingsubsystem.service.solution.SolutionService
import cz.upce.fei.testingsubsystem.service.authentication.UserAuthenticationService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api/v1/challenge/{challengeId}/solution")
@Tag(name = "Solution endpoint", description = "Solution upload (V1)")
class SolutionControllerV1(
    private val solutionService: SolutionService,
    private val authenticationService: UserAuthenticationService
) {
    @PostMapping("")
    @SolutionEndpointV1
    @CrossOrigin
    fun add(@PathVariable challengeId: Long, @RequestParam("file") file: MultipartFile, authentication: Authentication): ResponseEntity<*> {
        val result = solutionService.add(challengeId, authenticationService.extractAuthenticateUser(authentication), file)
        return ResponseEntity.ok(result.toDto())
    }
}