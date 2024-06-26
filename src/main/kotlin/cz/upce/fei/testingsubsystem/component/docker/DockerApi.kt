package cz.upce.fei.testingsubsystem.component.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import com.github.dockerjava.transport.DockerHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class DockerApi {
    @Bean
    @Scope("prototype")
    fun dockerHttpClient(config: DefaultDockerClientConfig): DockerHttpClient {
        return ApacheDockerHttpClient.Builder()
            .dockerHost(config.dockerHost)
            .sslConfig(config.sslConfig)
            .maxConnections(100)
            .connectionTimeout(Duration.ofSeconds(30))
            .responseTimeout(Duration.ofSeconds(45))
            .build()
    }

    @Bean
    @Scope("prototype")
    fun dockerClient(config: DefaultDockerClientConfig, dockerHttpClient: DockerHttpClient): DockerClient {
        return DockerClientImpl.getInstance(config, dockerHttpClient)
    }
}