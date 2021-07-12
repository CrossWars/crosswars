package xyz.crossward.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import xyz.crossward.service.StatsService

@RestController
@RequestMapping("/api/stats")
class StatsController(
    var service: StatsService
) {
    @GetMapping("/average")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getAverageTimeByUserId(
        @RequestParam("user", required = false) user: String?
    ): ResponseEntity<Double> {
        return ResponseEntity.ok(
            user?.let {
                service.getAverageTimeByUserId(it)
            }
        )
    }

    @GetMapping("/best")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getBestTimeByUserId(
        @RequestParam("user", required = false) user: String?
    ): ResponseEntity<Int> {
        return ResponseEntity.ok(
            user?.let {
                service.getBestTimeByUserId(it)
            }
        )
    }
}
