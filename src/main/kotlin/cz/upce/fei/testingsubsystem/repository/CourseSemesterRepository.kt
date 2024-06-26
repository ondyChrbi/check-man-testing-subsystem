package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.course.CourseSemester
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseSemesterRepository : JpaRepository<CourseSemester, Long> {
}