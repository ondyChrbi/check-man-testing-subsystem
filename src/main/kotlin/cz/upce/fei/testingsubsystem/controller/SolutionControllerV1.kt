package cz.upce.fei.testingsubsystem.controller

import cz.upce.fei.checkman.domain.course.security.annotation.ChallengeId
import cz.upce.fei.checkman.domain.course.security.annotation.SolutionId
import cz.upce.fei.testingsubsystem.doc.DownloadSolutionEndpointV1
import cz.upce.fei.testingsubsystem.doc.SolutionEndpointV1
import cz.upce.fei.testingsubsystem.service.authentication.AppUserAuthenticationService
import cz.upce.fei.testingsubsystem.service.authentication.annotation.PreCourseAuthorize
import cz.upce.fei.testingsubsystem.service.solution.SolutionService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Solution endpoint", description = "Solution upload (V1)")
class SolutionControllerV1(
    private val solutionService: SolutionService,
    private val authenticationService: AppUserAuthenticationService
) {
    @PostMapping("/challenge/{challengeId}/solution")
    @SolutionEndpointV1
    @CrossOrigin
    @PreCourseAuthorize
    fun add(@ChallengeId @PathVariable challengeId: Long, @RequestParam("file") file: MultipartFile, authentication: Authentication): ResponseEntity<*> {
        val appUser = authenticationService.extractAuthenticateUser(authentication)
        val result = solutionService.add(challengeId, appUser, file)

        return ResponseEntity.ok(result.toDto())
    }

    @GetMapping("/solution/{id}")
    @DownloadSolutionEndpointV1
    @CrossOrigin
    @PreCourseAuthorize
    fun findById(@SolutionId @PathVariable id: Long, authentication: Authentication): ResponseEntity<Resource> {
        val result = solutionService.downloadById(id)

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${result.fileName}")

        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(result.toFile().length())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(FileSystemResource(result.toFile()))
    }
}