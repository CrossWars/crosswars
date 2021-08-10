package xyz.crosswars.swagger

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.service.Tag
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.time.LocalDate

@EnableSwagger2
@Configuration
class SwaggerConfig {

    fun apiInfo() = ApiInfo(
        "Crosswars API",
        "Backend crossword REST API to store and maintain crossword data.",
        "1.0",
        "",
        Contact(
            "Crosswars Help",
            "crosswars.xyz/help",
            "crosswars.help@gmail.com"
        ),
        "",
        "",
        emptyList()
    )

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("xyz.crosswars"))
            .paths(PathSelectors.any())
            .build()
            .pathMapping("/")
            .directModelSubstitute(LocalDate::class.java, String::class.java)
            .genericModelSubstitutes(ResponseEntity::class.java)
            .useDefaultResponseMessages(false)
            .enableUrlTemplating(false)
            .tags(
                Tag("Users", "Create and update user information"),
                Tag("Entries", "Create and update entries"),
                Tag("Groups", "Create and update groups"),
                Tag("Stats", "Get various statistical information"),
                Tag("Wins", "Create and update wins"),
            )
    }
}