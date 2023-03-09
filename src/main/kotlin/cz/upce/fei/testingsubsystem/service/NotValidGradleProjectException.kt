package cz.upce.fei.testingsubsystem.service

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class NotValidGradleProjectException : Exception("Zip not containing valid Gradle project")
