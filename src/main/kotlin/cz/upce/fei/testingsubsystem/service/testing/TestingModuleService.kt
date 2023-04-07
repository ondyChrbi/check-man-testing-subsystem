package cz.upce.fei.testingsubsystem.service.testing

import cz.upce.fei.testingsubsystem.service.testing.annotation.TestingModule
import cz.upce.fei.testingsubsystem.service.testing.exception.ClassNotAnnotatedAsTestModuleException
import cz.upce.fei.testingsubsystem.service.testing.exception.DockerFileNotSetException
import cz.upce.fei.testingsubsystem.service.testing.exception.TestModuleClassNotFoundException
import org.reflections.Reflections
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service

@Service
class TestingModuleService(
    private val applicationContext: ApplicationContext
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${my.group}")
    private var rootPackageName: String = "cz.upce.fei"

    fun findTestingModules(): Set<Class<*>> {
        val reflections = Reflections(
            ConfigurationBuilder()
            .setUrls(ClasspathHelper.forPackage(rootPackageName))
            .filterInputsBy { it.startsWith(rootPackageName) }
        )

        return reflections.getTypesAnnotatedWith(TestingModule::class.java)
    }

    fun validateTestingModule(className: String?) {
        if (className.isNullOrEmpty()) {
            throw TestModuleClassNotFoundException(className)
        }

        val testModule = applicationContext.getBean(Class.forName(className))
            ?: throw TestModuleClassNotFoundException(className)

        validateTestingModule(testModule::class.java)
    }

    fun validateTestingModule(testingModule: Class<*>) {
        val annotation = testingModule.getAnnotation(TestingModule::class.java)
            ?: throw ClassNotAnnotatedAsTestModuleException(testingModule.name)

        if (annotation.dockerFilePath.isEmpty()) {
            throw DockerFileNotSetException(annotation::class.java)
        }
    }

    fun logTestingModule(testingModule: Class<*>) {
        val annotation = testingModule.getAnnotation(TestingModule::class.java)
            ?: throw ClassNotAnnotatedAsTestModuleException(testingModule.name)

        logTestingModule(annotation)
    }

    fun logTestingModule(module: TestingModule) {
        logger.info("Found testing module: $module")
    }
}