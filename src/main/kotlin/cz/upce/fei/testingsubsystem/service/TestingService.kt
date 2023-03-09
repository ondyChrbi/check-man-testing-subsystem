package cz.upce.fei.testingsubsystem.service

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths

@Service
class TestingService {
    @Value("\${check-man.solution.location}")
    private lateinit var filesLocation : String

    @PostConstruct
    fun init() {
        Files.createDirectories(Paths.get(filesLocation))
    }


}