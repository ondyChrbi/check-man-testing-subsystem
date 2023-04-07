package cz.upce.fei.testingsubsystem.component.testing.module.gradle

import cz.upce.fei.testingsubsystem.domain.testing.Feedback
import org.w3c.dom.Element

data class JunitTestCase(
    val classname: String,
    val name: String,
    val time: Float,
    val status: TestStatus = TestStatus.PASSED
) {
    fun toFeedback() : Feedback {
        return Feedback(
            description = "$classname-$name",
            feedbackTypeId = Feedback.TEST_CASE_MAP[status]!!.id
        )
    }

    enum class TestStatus {
        PASSED, FAILED, ERRORED
    }

    companion object {
        fun parseStatus(testCase: Element): TestStatus {
            val failureNodes = testCase.getElementsByTagName("failure")
            val errorNodes = testCase.getElementsByTagName("error")

            val status = when {
                failureNodes.length > 0 -> TestStatus.FAILED
                errorNodes.length > 0 -> TestStatus.ERRORED
                else -> TestStatus.PASSED
            }

            return status
        }
    }
}