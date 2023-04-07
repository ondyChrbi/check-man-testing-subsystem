package cz.upce.fei.testingsubsystem.service.testing

import cz.upce.fei.testingsubsystem.domain.testing.Solution

class SolutionFileNotFoundException(solution: Solution) : Exception("""
    Failed of testing subsystem. File for $solution not found.
""".trimIndent())