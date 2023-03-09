package cz.upce.fei.testingsubsystem.controller

import cz.upce.fei.testingsubsystem.doc.TemplateEndpointV1
import cz.upce.fei.testingsubsystem.service.TemplateService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URI

@RestController
@RequestMapping("/api/v1/template")
@Tag(name = "Testing template endpoint", description = "Testing project template (V1)")
class TemplateControllerV1(
    private val templateService: TemplateService
) {
    private var contextPath : String = ""

    @PostMapping("")
    @TemplateEndpointV1
    fun add(@RequestParam("file") file: MultipartFile): ResponseEntity<*> {
        val fileName = templateService.add(file)

        return ResponseEntity.created(URI("${contextPath}/template/${fileName}"))
            .build<String>()
    }
}