package cz.upce.fei.testingsubsystem.config

import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.core.DefaultDockerClientConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DockerApiConfig {
    @Bean
    fun defaultDockerClientConfig() : DefaultDockerClientConfig {
        return DefaultDockerClientConfig.createDefaultConfigBuilder().build()
    }

    @Bean
    fun hostConfig() : HostConfig {
        return HostConfig.newHostConfig()
            .withAutoRemove(false)
            .withPublishAllPorts(true)
            .withVolumesFrom()
            .withNetworkMode("bridge")
    }
}