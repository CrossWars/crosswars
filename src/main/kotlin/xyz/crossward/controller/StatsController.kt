package xyz.crossward.controller

import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import xyz.crossward.service.StatsService

@Api(tags = ["Stats"])
@RestController
@RequestMapping("/api/stats")
class StatsController(
    var service: StatsService
) {
    @GetMapping("/averages")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getAverageTimeByUserId(
        @RequestParam("user_id", required = false) userId: String?
    ): ResponseEntity<Double> {
        return ResponseEntity.ok(
            userId?.let {
                service.getAverageTimeByUserId(it)
            }
        )
    }

    @GetMapping("/best_times")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getBestTimeByUserId(
        @RequestParam("user_id", required = false) userId: String?
    ): ResponseEntity<Int> {
        return ResponseEntity.ok(
            userId?.let {
                service.getBestTimeByUserId(it)
            }
        )
    }
}
