package cz.upce.fei.testingsubsystem.domain.course

import cz.upce.fei.testingsubsystem.domain.user.AppUserCourseSemesterRole
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table
data class CourseSemester(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @field:Column
    var note: String? = null,

    @field:Column
    var dateStart: LocalDateTime,

    @field:Column
    var dateEnd: LocalDateTime? = null,

    @field:OneToMany(mappedBy = "semester", fetch = FetchType.LAZY)
    var challenge: List<Challenge> = mutableListOf(),

    @field:OneToMany(mappedBy="courseSemester", fetch = FetchType.LAZY)
    var appUserCourseSemesterRole: MutableList<AppUserCourseSemesterRole>? = mutableListOf(),
)