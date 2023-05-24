package cz.upce.fei.testingsubsystem.service.authentication.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class AppUserCourseSemesterForbiddenException : Throwable() {

}
