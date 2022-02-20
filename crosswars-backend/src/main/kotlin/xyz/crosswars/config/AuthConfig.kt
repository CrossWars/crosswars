package xyz.crosswars.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "auth")
class AuthConfig(
    var enabled: Boolean = true,
    var googleClientId: String = "474023833184-4odp7ummj7puclc850bo80brkcj10k4j.apps.googleusercontent.com",
    var authKey: String = "CyAoCucPLH189AiR-Z59fG89"
)