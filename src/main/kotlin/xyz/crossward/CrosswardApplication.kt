package xyz.crossward

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CrosswardApplication

fun main(args: Array<String>) {
    runApplication<CrosswardApplication>(*args)
}
