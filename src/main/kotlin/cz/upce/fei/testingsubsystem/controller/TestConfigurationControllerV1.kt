package cz.upce.fei.testingsubsystem.controller

import cz.upce.fei.testingsubsystem.doc.*
import cz.upce.fei.testingsubsystem.dto.TestConfigurationInputDtoV1
import cz.upce.fei.testingsubsystem.service.TestConfigurationService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Testing configuration endpoint", description = "Testing project template (V1)")
class TestConfigurationControllerV1(private val testConfigurationService: TestConfigurationService) {
    @GetMapping("/challenge/{challengeId}/template")
    @AvailableTemplatesEndpointV1
    @CrossOrigin
    fun findAll(@PathVariable challengeId: Long): ResponseEntity<*> {
        val result = testConfigurationService.findAll(challengeId)

        return ResponseEntity.ok(result.map { it.toDto() })
    }

    @GetMapping("/challenge/{challengeId}/test-configuration")
    @TestConfigurationEndpointV1
    @CrossOrigin
    fun find(@PathVariable challengeId: Long): ResponseEntity<*> {
        val result = testConfigurationService.findByChallenge(challengeId)

        return ResponseEntity.ok(result?.toDto())
    }

    @PostMapping("/challenge/{challengeId}/test-configuration")
    @CreateTestConfigurationEndpointV1
    @CrossOrigin
    fun addTestConfiguration(@PathVariable challengeId: Long, @RequestBody testConfigurationDto: TestConfigurationInputDtoV1): ResponseEntity<*> {
        val result = testConfigurationService.add(testConfigurationDto.toEntity(), challengeId)

        return ResponseEntity.ok(result.toDto())
    }

    @PatchMapping("/test-configuration/{testConfigurationId}/template-path")
    @TemplateEndpointV1
    @CrossOrigin
    fun addTemplate(@PathVariable testConfigurationId: Long, @RequestParam("file") file: MultipartFile): ResponseEntity<*> {
        val result = testConfigurationService.addTemplate(file, testConfigurationId)

        return ResponseEntity.ok(result.toDto())
    }

    @PatchMapping("/test-configuration/{testConfigurationId}/docker-file")
    @TemplateEndpointV1
    @CrossOrigin
    fun addDocker(@PathVariable testConfigurationId: Long, @RequestParam("file") file: MultipartFile): ResponseEntity<*> {
        val result = testConfigurationService.addDockerFile(testConfigurationId, file)

        return ResponseEntity.ok(result.toDto())
    }
}