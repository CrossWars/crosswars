package xyz.crossward.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "google")
class GoogleAuthConfig(
    var clientId: String = ""
)