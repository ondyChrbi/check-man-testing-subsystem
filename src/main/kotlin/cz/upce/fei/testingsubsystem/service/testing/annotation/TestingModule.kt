package cz.upce.fei.testingsubsystem.service.testing.annotation
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Component
@Scope("prototype")
annotation class TestingModule(
    val key: String = "none",
    val name: String = "",
    val description: String = "",
    val dockerFilePath: String = "",
)