package cz.upce.fei.testingsubsystem.service.authentication

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class WrongSecurityPrincipalsException : Exception() {

}
