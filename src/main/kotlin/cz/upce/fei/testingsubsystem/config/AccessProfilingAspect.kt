package cz.upce.fei.testingsubsystem.config

import cz.upce.fei.checkman.domain.course.security.annotation.ChallengeId
import cz.upce.fei.checkman.domain.course.security.annotation.NotIdDataTypeException
import cz.upce.fei.checkman.domain.course.security.annotation.SolutionId
import cz.upce.fei.testingsubsystem.domain.user.AppUser
import cz.upce.fei.testingsubsystem.service.authentication.AppUserAuthenticationService
import cz.upce.fei.testingsubsystem.service.authentication.exception.AppUserCourseSemesterForbiddenException
import cz.upce.fei.testingsubsystem.service.course.CourseSemesterRoleService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.Authentication

@Aspect
@Configuration
class AccessProfilingAspect(
    private val authenticationService: AppUserAuthenticationService,
    private val courseSemesterRoleService: CourseSemesterRoleService,
) {
    @Around("@annotation(cz.upce.fei.testingsubsystem.service.authentication.annotation.PreCourseAuthorize)")
    fun checkSubmitAccess(joinPoint: ProceedingJoinPoint): Any? {
        val authentication = joinPoint.args.first { it is Authentication } as Authentication
        val appUser = authenticationService.extractAuthenticateUser(authentication)

        if (check(joinPoint, appUser)) return joinPoint.proceed()

        throw AppUserCourseSemesterForbiddenException()
    }

    private fun check(joinPoint: ProceedingJoinPoint, appUser: AppUser): Boolean {
        val methodSignature = joinPoint.signature as MethodSignature
        val parameters = methodSignature.method.parameters

        val challenges = parameters.filter { it.annotations.filterIsInstance<ChallengeId>().isNotEmpty() }
        if (challenges.isNotEmpty()) {
            val challengeId = joinPoint.args[parameters.indexOf(challenges.first())]
            if (challengeId !is Long) {
                throw NotIdDataTypeException("challengeId", Long::class.java)
            }

            if (!checkBasedChallenge(challengeId, appUser)) {
                throw AppUserCourseSemesterForbiddenException()
            }

            return true
        }

        val solutions = parameters.filter { it.annotations.filterIsInstance<SolutionId>().isNotEmpty() }
        if (solutions.isNotEmpty()) {
            val solutionId = joinPoint.args[parameters.indexOf(solutions.first())]
            if (solutionId !is Long) {
                throw NotIdDataTypeException("solutionId", Long::class.java)
            }

            if (!checkBasedSolution(solutionId, appUser)) {
                throw AppUserCourseSemesterForbiddenException()
            }

            return true
        }

        return false
    }

    private fun checkBasedChallenge(challengeId: Long, appUser: AppUser): Boolean {
        return courseSemesterRoleService.hasAppUserRolesChallenge(challengeId, appUser, listOf("EDIT_CHALLENGE"))
    }

    private fun checkBasedSolution(solutionId: Long, appUser: AppUser): Boolean {
        return courseSemesterRoleService.hasAppUserRolesSolution(solutionId, appUser, listOf("EDIT_CHALLENGE"))
    }
}