package cz.upce.fei.testingsubsystem.controller

import cz.upce.fei.testingsubsystem.doc.AvailableTestingModulesEndpointV1
import cz.upce.fei.testingsubsystem.service.testing.TestingModuleDto
import cz.upce.fei.testingsubsystem.service.testing.TestingModuleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/test-module")
class TestingModuleControllerV1(
    private val testingModuleService: TestingModuleService
) {
    @GetMapping("")
    @AvailableTestingModulesEndpointV1
    @CrossOrigin
    fun findAll(): ResponseEntity<*> {
        val result = testingModuleService.findTestingModules()

        return ResponseEntity.ok(result.mapNotNull { TestingModuleDto.from(it) })
    }
}