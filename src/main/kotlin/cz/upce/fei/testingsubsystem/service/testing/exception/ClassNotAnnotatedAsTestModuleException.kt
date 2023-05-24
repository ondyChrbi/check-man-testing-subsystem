package cz.upce.fei.testingsubsystem.service.testing.exception

class ClassNotAnnotatedAsTestModuleException(name: String) :
    Throwable("Class ${name} is not annotated with @TestingModule")