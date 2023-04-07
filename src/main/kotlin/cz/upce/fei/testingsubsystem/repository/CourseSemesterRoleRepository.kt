package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.course.CourseSemester
import cz.upce.fei.testingsubsystem.domain.course.CourseSemesterRole
import cz.upce.fei.testingsubsystem.domain.user.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CourseSemesterRoleRepository : JpaRepository<CourseSemesterRole, Long> {
    @Query("""
        select r from CourseSemesterRole r
        inner join AppUserCourseSemesterRole aucsr
        where aucsr.courseSemester = :courseSemester and aucsr.appUser = :appUser
    """)
    fun findAllByCourseSemesterAndAppUser(courseSemester: CourseSemester, appUser : AppUser) : List<CourseSemesterRole>
}
