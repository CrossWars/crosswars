package xyz.crossward.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CrosswardController {

    @GetMapping
    fun helloCrossward(): String {
        return "Hello Crossward!"
    }
}