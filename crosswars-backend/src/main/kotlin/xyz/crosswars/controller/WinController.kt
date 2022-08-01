package xyz.crosswars.controller

import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.*
import xyz.crosswars.entities.Win
import xyz.crosswars.entities.WinCount
import xyz.crosswars.entities.Winner
import xyz.crosswars.service.WinService
import javax.transaction.Transactional

@Api(tags = ["Wins"])
@RestController
@RequestMapping("/wins")
class WinController(
    private val winService: WinService
) {
    /**
     * Record a single win to the database.
     *
     * Creates a new record if the user has never won before.
     * Increases the number of wins if the user has an entry.
     *
     * @param win request object containing the win data
     */
    @PostMapping
    fun recordWin(@RequestBody win: Win): Win {
        return winService.recordWin(win)
    }

    /**
     * Get the winner in a group for the given date
     *
     * @param groupId the group id the user is a member of
     * @param date date of the win
     */
    @GetMapping("/winners")
    fun getWin(
        @RequestParam("group_id") groupId: String,
        @RequestParam("date", required = false) date: String?
    ): Winner {
        return winService.getWinner(groupId, date)
    }

    /**
     * Get the win count for a single user in a given group
     *
     * @param userId user id
     * @param groupId group id
     * @param fromDate the date to start counting wins from (inclusive)
     * @param toDate the date to start counting wins to (inclusive)
     * @return a WinCount object
     */
    @GetMapping("/counts")
    fun getWinCount(
        @RequestParam("user_id") userId: String,
        @RequestParam("group_id") groupId: String,
        @RequestParam("from_date", required = false) fromDate: String?,
        @RequestParam("to_date", required = false) toDate: String?,
    ): WinCount {
        return winService.getWinCount(userId, groupId, fromDate, toDate)
    }

    @GetMapping("/counts/groups")
    @Transactional
    fun getWinCountForAllUsersInGroup(
        @RequestParam("group_id") groupId: String
    ): List<WinCount> {
        return winService.getWinCountsForAllUsersInGroup(groupId)
    }

    @GetMapping("/groups")
    @Transactional
    fun getWinsForAllUsersInGroup(
            @RequestParam("group_id") groupId: String
    ): List<Win> {
        return winService.getWinsForAllUsersInGroup(groupId)
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
    fun updateWinRecord(@RequestBody wins: Win): Win {
        return winService.updateWinsRecord(wins)
    }

    /**
     * Utility endpoint for recalculating and overwriting all wins in a group
     */
    @PutMapping("/sync")
    fun syncWinsInGroup(
            @RequestParam( "group_id") groupId: String
    ): List<WinCount> {
        return winService.syncWinsInGroup(groupId)
    }
}