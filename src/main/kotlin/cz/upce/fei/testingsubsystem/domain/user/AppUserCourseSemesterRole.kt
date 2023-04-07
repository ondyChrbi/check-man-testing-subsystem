package cz.upce.fei.testingsubsystem.domain.user

import cz.upce.fei.testingsubsystem.domain.course.CourseSemester
import cz.upce.fei.testingsubsystem.domain.course.CourseSemesterRole
import jakarta.persistence.*

@Entity
@Table
data class AppUserCourseSemesterRole(
    @field:Id @field:GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0,

    @field:ManyToOne(fetch = FetchType.EAGER)
    @field:JoinColumn(name = "app_user_id")
    var appUser: AppUser? = null,

    @field:ManyToOne(fetch = FetchType.EAGER)
    @field:JoinColumn(name = "course_semester_id")
    var courseSemester: CourseSemester? = null,

    @field:ManyToOne(fetch = FetchType.EAGER)
    @field:JoinColumn(name = "course_semester_role_id")
    var courseSemesterRole: CourseSemesterRole? = null
)