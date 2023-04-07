package cz.upce.fei.testingsubsystem.controller

import cz.upce.fei.testingsubsystem.doc.AvailableTemplatesEndpointV1
import cz.upce.fei.testingsubsystem.doc.DockerEndpointV1
import cz.upce.fei.testingsubsystem.doc.TemplateEndpointV1
import cz.upce.fei.testingsubsystem.service.TemplateService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Testing template endpoint", description = "Testing project template (V1)")
class TemplateControllerV1(private val templateService: TemplateService) {
    @GetMapping("/challenge/{challengeId}/template")
    @AvailableTemplatesEndpointV1
    @CrossOrigin
    fun findAll(@PathVariable challengeId: Long): ResponseEntity<*> {
        val result = templateService.findAll(challengeId)

        return ResponseEntity.ok(result.map { it.toDto() })
    }

    @PostMapping("/challenge/{challengeId}/template")
    @TemplateEndpointV1
    @CrossOrigin
    fun add(@PathVariable challengeId: Long, @RequestParam("file") file: MultipartFile): ResponseEntity<*> {
        val result = templateService.add(file, challengeId)

        return ResponseEntity.ok(result.toDto())
    }

    @PutMapping("/template/{id}/dockerFile")
    @DockerEndpointV1
    @CrossOrigin
    fun addDocker(@PathVariable id: Long, @RequestParam("file") file: MultipartFile): ResponseEntity<*> {
        val result = templateService.addDockerFile(id, file)

        return ResponseEntity.ok(result.toDto())
    }
}