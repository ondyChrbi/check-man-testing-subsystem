package cz.upce.fei.testingsubsystem.service.course

import cz.upce.fei.testingsubsystem.repository.CourseSemesterRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CourseSemesterService(private val courseSemesterRepository: CourseSemesterRepository) {
    @Transactional
    fun findById(id: Long) = courseSemesterRepository.findById(id)
}