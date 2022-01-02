package xyz.crosswars.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AppConfig(
    private val authorizationInterceptor: AuthorizationInterceptor,
    private val authorizationNewUserInterceptor: AuthorizationNewUserInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authorizationInterceptor)
        registry.addInterceptor(authorizationNewUserInterceptor)
    }
}