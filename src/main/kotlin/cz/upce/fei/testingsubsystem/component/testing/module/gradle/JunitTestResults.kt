package cz.upce.fei.testingsubsystem.component.testing.module.gradle

data class JunitTestResults(
    val tests: Int,
    val failures: Int,
    val errors: Int,
    val skipped: Int,
    val time: Float,
    val testCases: List<JunitTestCase>
)