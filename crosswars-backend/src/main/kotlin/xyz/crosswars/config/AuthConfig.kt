package xyz.crosswars.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "auth")
class AuthConfig(
    var enabled: Boolean = true,
    var googleClientId: String = "",
    var authKey: String = ""
)