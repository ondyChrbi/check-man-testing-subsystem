package cz.upce.fei.testingsubsystem.controller

import cz.upce.fei.testingsubsystem.doc.SolutionEndpointV1
import cz.upce.fei.testingsubsystem.service.TemplateService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/api/v1/solution")
@Tag(name = "Solution endpoint", description = "Solution upload (V1)")
class SolutionControllerV1(private val templateService: TemplateService) {
    private var contextPath : String = ""

    @PostMapping("")
    @SolutionEndpointV1
    fun add(@RequestParam("file") file: MultipartFile): ResponseEntity<*> {
        val type = TemplateService.Type.SOLUTION
        val fileName = templateService.add(file, type)

        return ResponseEntity.created(URI("${contextPath}/${type.toString().lowercase()}/${fileName}"))
            .build<String>()
    }
}