package cz.upce.fei.testingsubsystem.service.course

import cz.upce.fei.testingsubsystem.domain.user.AppUser
import cz.upce.fei.testingsubsystem.repository.CourseSemesterRoleRepository
import cz.upce.fei.testingsubsystem.service.solution.SolutionService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CourseSemesterRoleService(
    private val semesterService: SemesterService,
    private val challengeService: ChallengeService,
    private val solutionService: SolutionService,
    private val courseSemesterRoleRepository: CourseSemesterRoleRepository
) {
    @Transactional
    fun hasAppUserRoles(semesterId: Long, appUser: AppUser, requestedRoles: List<String>): Boolean {
        val course = semesterService.findById(semesterId)
        if (course.isEmpty) { return false }

        val roles = courseSemesterRoleRepository.findAllByCourseSemesterAndAppUser(course.get(), appUser)

        return roles.map { it.name }.containsAll(requestedRoles)
    }

    @Transactional
    fun hasAppUserRolesChallenge(challengeId: Long, appUser: AppUser, requestedRoles: List<String>): Boolean {
        val challenge = challengeService.findById(challengeId)

        val roles = courseSemesterRoleRepository.findAllByChallengeAndAppUser(challenge, appUser)

        return roles.map { it.name }.containsAll(requestedRoles)
    }

    @Transactional
    fun hasAppUserRolesSolution(solutionId: Long, appUser: AppUser, requestedRoles: List<String>): Boolean {
        val solution = solutionService.findById(solutionId)

        val roles = courseSemesterRoleRepository.findAllBySolutionAndAppUser(solution, appUser)

        val hasRole = roles.map { it.name }.containsAll(requestedRoles)
        val isAuthor = solution.user == appUser

        return hasRole || isAuthor
    }
}