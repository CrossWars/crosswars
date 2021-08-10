package xyz.crosswars

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CrosswarsApplication

fun main(args: Array<String>) {
    runApplication<CrosswarsApplication>(*args)
}
