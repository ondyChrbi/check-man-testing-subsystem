package cz.upce.fei.testingsubsystem.service.testing.exception

import cz.upce.fei.testingsubsystem.component.testing.module.TestModule
import cz.upce.fei.testingsubsystem.service.testing.annotation.TestingModule
import java.lang.Exception

class TestModuleNotFoundException(testModule: String) : Exception("""
    Test module $testModule not found.
    Please check if the test module implements ${TestModule::class.java} interface and it is annotated with ${TestingModule::class.java}.
""".trimIndent())