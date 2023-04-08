package cz.upce.fei.testingsubsystem.component.testing.module.gradle

import com.github.dockerjava.api.model.Bind
import com.github.dockerjava.api.model.Volume
import cz.upce.fei.testingsubsystem.component.testing.module.TestModule
import cz.upce.fei.testingsubsystem.component.testing.module.gradle.JunitTestCase.Companion.parseStatus
import cz.upce.fei.testingsubsystem.domain.testing.Feedback
import cz.upce.fei.testingsubsystem.domain.testing.Solution
import cz.upce.fei.testingsubsystem.domain.testing.TestResult
import cz.upce.fei.testingsubsystem.service.testing.annotation.TestingModule
import cz.upce.fei.testingsubsystem.service.testing.docker.DockerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.w3c.dom.Element
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.streams.asSequence

@TestingModule(
    key = "gradle-java-kotlin",
    dockerFilePath = "dockerfile/DockerFile-Gradle-Module",
    name = "Gradle Java/Kotlin",
    description = "Gradle Java/Kotlin testing module"
)
class GradleModule(private val dockerService: DockerService) : TestModule {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${check-man.modules.gradle.volume.container-path}")
    private var volumeContainerPath : String = ""

    @Value("\${check-man.modules.gradle.container.timeout}")
    private var containerTimeout: Int = 30

    override fun test(dockerFile: Path, testResult: TestResult?, resultPath: Path): Path {
        super.test(dockerFile, testResult, resultPath)

        val imageName = "$GRADLE_CONTAINER_PREFIX-${UUID.randomUUID()}"

        val log = StringBuffer("")
        val imageId = dockerService.buildImage(dockerFile, imageName, testResult)

        val binds = listOf(Bind(resultPath.toAbsolutePath().toString(), Volume(volumeContainerPath)))

        val containerId = dockerService.runContainer(imageName, testResult, binds.toList(), containerTimeout, log)
        logger.info("Container for docker file $dockerFile (image id: $imageId) finished with container id: $containerId")
        logger.info("Result is available under $resultPath")

        return resultPath
    }

    override fun resultToFeedbacks(resultPath: Path, solution: Solution): Collection<Feedback> {
        return findXmlFiles(resultPath)
            .map { parseTestResults(it) }
            .map { it.testCases }
            .flatMap { cases -> cases.map { it.toFeedback() } }
            .toSet()
    }

    private fun findXmlFiles(directoryPath: Path): List<Path> {
        return Files.walk(directoryPath)
            .asSequence()
            .filter { Files.isRegularFile(it) && it.toString().endsWith(".xml") }
            .toList()
    }

    private fun parseTestResults(path: Path): JunitTestResults {
        val xmlFile = File(path.toUri())

        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.parse(xmlFile)

        val testSuiteNode = document.getElementsByTagName("testsuite").item(0)
        val testSuiteAttributes = testSuiteNode.attributes

        val tests = testSuiteAttributes.getNamedItem("tests").nodeValue.toInt()
        val failures = testSuiteAttributes.getNamedItem("failures").nodeValue.toInt()
        val errors = testSuiteAttributes.getNamedItem("errors").nodeValue.toInt()
        val skipped = testSuiteAttributes.getNamedItem("skipped").nodeValue.toInt()
        val time = testSuiteAttributes.getNamedItem("time").nodeValue.toFloat()

        val testCases = mutableListOf<JunitTestCase>()
        val testCaseNodes = document.getElementsByTagName("testcase")

        for (i in 0 until testCaseNodes.length) {
            val testCase = testCaseNodes.item(i) as Element
            val testCaseAttributes = testCase.attributes

            val classname = testCaseAttributes.getNamedItem("classname").nodeValue
            val name = testCaseAttributes.getNamedItem("name").nodeValue
            val caseTime = testCaseAttributes.getNamedItem("time").nodeValue.toFloat()
            val status = parseStatus(testCase)

            testCases.add(JunitTestCase(classname, name, caseTime, status))
        }

        return JunitTestResults(tests, failures, errors, skipped, time, testCases)
    }

    object DockerCommands {
        val GRADLE_TEST = mutableListOf("./gradlew", ":app:test", "--info")
    }

    private companion object {
        const val GRADLE_CONTAINER_PREFIX = "checkman-gradle"
    }
}