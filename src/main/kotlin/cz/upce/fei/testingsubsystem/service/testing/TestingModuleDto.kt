package cz.upce.fei.testingsubsystem.service.testing

import cz.upce.fei.testingsubsystem.service.testing.annotation.TestingModule

data class TestingModuleDto(
    var key: String,
    var name: String,
    var description: String,
    var dockerFilePath: String,
    var moduleClass: String,
) {
    companion object {
        private fun from(annotation: TestingModule, clazz: Class<*>): TestingModuleDto {
            return TestingModuleDto(
                key = annotation.key,
                name = annotation.name,
                description = annotation.description,
                dockerFilePath = annotation.dockerFilePath,
                moduleClass = clazz.name,
            )
        }

        fun from(moduleClass: Class<*>): TestingModuleDto? {
            val annotation = moduleClass.getAnnotation(TestingModule::class.java) ?: return null
            return from(annotation, moduleClass)
        }
    }
}