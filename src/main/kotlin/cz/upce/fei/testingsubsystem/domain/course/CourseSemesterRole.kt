package cz.upce.fei.testingsubsystem.domain.course

import cz.upce.fei.testingsubsystem.domain.user.AppUser
import jakarta.persistence.*

@Entity
@Table
data class CourseSemesterRole(
    @field:Id private var id: Long,
    @field:Column var name: String,
    @field:ManyToMany(mappedBy = "courseSemesterRoles", fetch = FetchType.LAZY)
    var appUsers: MutableCollection<AppUser> = mutableListOf()
) {
    override fun toString(): String {
        return "Role(id=$id, name='$name')"
    }
}