package cz.upce.fei.testingsubsystem.service.course

import cz.upce.fei.testingsubsystem.domain.user.AppUser
import cz.upce.fei.testingsubsystem.repository.CourseSemesterRoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CourseSemesterRoleService(
    private val semesterService: SemesterService,
    private val courseSemesterRoleRepository: CourseSemesterRoleRepository
) {
    @Transactional
    fun hasAppUserRoles(semesterId: Long, appUser: AppUser, requestedRoles: List<String>): Boolean {
        val course = semesterService.findById(semesterId)
        if (course.isEmpty) { return false }

        val roles = courseSemesterRoleRepository.findAllByCourseSemesterAndAppUser(course.get(), appUser)

        return roles.map { it.name }.containsAll(requestedRoles)
    }
}