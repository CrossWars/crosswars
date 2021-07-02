package xyz.crossward.controller

import org.springframework.web.bind.annotation.*
import xyz.crossward.entities.Wins
import xyz.crossward.service.WinsService

@RestController
@RequestMapping("/api/wins")
class WinsController(
    private val winsService: WinsService
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
    fun recordWin(@RequestBody wins: Wins): Wins {
        return winsService.recordWin(wins)
    }

    /**
     * Get wins for a user in a specific group
     *
     * @param userId the user id
     * @param groupId the group id the user is a member of
     */
    @GetMapping("/{userId}/{groupId}")
    fun getWins(
        @PathVariable("userId") userId: String,
        @PathVariable("groupId") groupId: String
    ): Wins {
        return winsService.getWins(userId, groupId)
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
    fun updateWinsRecord(@RequestBody wins: Wins): Wins {
        return winsService.updateWinsRecord(wins)
    }
}