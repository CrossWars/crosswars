package xyz.crosswars

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class CrosswarsApplication

fun main(args: Array<String>) {
    runApplication<CrosswarsApplication>(*args)
}
