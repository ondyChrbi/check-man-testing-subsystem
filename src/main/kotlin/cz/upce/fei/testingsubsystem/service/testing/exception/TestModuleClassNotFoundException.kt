package cz.upce.fei.testingsubsystem.service.testing.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class TestModuleClassNotFoundException(className: String?) : Exception("Test module class $className not found")
