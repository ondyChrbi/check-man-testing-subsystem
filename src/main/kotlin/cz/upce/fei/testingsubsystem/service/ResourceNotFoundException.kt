package cz.upce.fei.testingsubsystem.service

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.Exception

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(clazz: Class<*>, id: Long) : Exception("Resource ${clazz.simpleName} with id $id not found")