package cz.upce.fei.testingsubsystem.dto

import java.time.LocalDateTime

data class SolutionDtoV1(
    var id: Long,
    var path: String,
    var uploadDate: LocalDateTime
)