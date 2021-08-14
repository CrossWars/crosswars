package xyz.crosswars.controller

import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.*
import xyz.crosswars.entities.Win
import xyz.crosswars.entities.WinCount
import xyz.crosswars.service.WinService
import xyz.crosswars.util.getPuzzleDateInEST

@Api(tags = ["Wins"])
@RestController
@RequestMapping("/api/wins")
class WinController(
    private val winService: WinService
) {
    /**
     * Record a single win to the database.
     *
     * Creates a new record if the user has never won before.
     * Increases the number of wins if the user has an entry.
     *
     * @param wins request object containing the win data
     */
    @PostMapping
    fun recordWin(@RequestBody wins: Win): Win {
        return winService.recordWin(wins)
    }

    /**
     * Get wins for a user in a specific group
     *
     * @param userId the user id
     * @param groupId the group id the user is a member of
     */
    @GetMapping
    fun getWin(
        @RequestParam("user_id") userId: String,
        @RequestParam("group_id") groupId: String,
        @RequestParam("date", required = false) date: String?
    ): Win {
        val win = Win(
            userId,
            groupId,
            date = date ?: getPuzzleDateInEST()
        )
        return winService.getWins(win)
    }

    @GetMapping("/counts")
    fun getWinCount(
        @RequestParam("user_id") userId: String,
        @RequestParam("group_id") groupId: String,
        @RequestParam("from_date", required = false) fromDate: String?,
        @RequestParam("to_date", required = false) toDate: String?,
    ): WinCount {
        return winService.getWinCount(userId, groupId, fromDate, toDate)
    }

    /**
     * Utility endpoint to manually update a wins record in the database.
     *
     * WARNING: This will overwrite the data for the user!
     * If you just need to add 1 win use the POST endpoint.
     *
     * @param wins a wins object
     * @return the record saved to the database
     */
    @PutMapping
    fun updateWinRecord(@RequestBody wins: Win): Win {
        return winService.updateWinsRecord(wins)
    }
}