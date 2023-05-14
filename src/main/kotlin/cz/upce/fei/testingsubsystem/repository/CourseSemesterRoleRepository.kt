package cz.upce.fei.testingsubsystem.repository

import cz.upce.fei.testingsubsystem.domain.course.Challenge
import cz.upce.fei.testingsubsystem.domain.course.CourseSemester
import cz.upce.fei.testingsubsystem.domain.course.CourseSemesterRole
import cz.upce.fei.testingsubsystem.domain.testing.Solution
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

    @Query("""
        select distinct r from CourseSemesterRole r
        inner join AppUserCourseSemesterRole aucsr
        inner join CourseSemester cs
        inner join Challenge ch
        where ch = :challenge and aucsr.appUser = :appUser
    """)
    fun findAllByChallengeAndAppUser(challenge: Challenge, appUser : AppUser) : List<CourseSemesterRole>

    @Query("""
        select distinct r from CourseSemesterRole r
        inner join AppUserCourseSemesterRole aucsr
        inner join Solution s
        inner join AppUser ap
        inner join CourseSemester cs
        where s = :solution and s.user = :appUser
    """)
    fun findAllBySolutionAndAppUser(solution: Solution, appUser : AppUser) : List<CourseSemesterRole>

}
