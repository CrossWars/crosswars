package xyz.crosswars.controller

import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.server.ResponseStatusException
import xyz.crosswars.config.Authorized
import xyz.crosswars.entities.Entry
import xyz.crosswars.entities.TelegramEntry
import xyz.crosswars.entities.User
import xyz.crosswars.exception.NoContentException
import xyz.crosswars.service.EntryService
import xyz.crosswars.service.UserService


@Api(tags = ["Entries"])
@RestController
@RequestMapping("/entries")
class EntryController(
    private val entryService: EntryService,
) {
    @PostMapping
    @Authorized
    fun recordEntry(@RequestBody entry: Entry, @RequestAttribute("auth_user") auth_user: User?): Entry {
        return entryService.recordEntry(entry, auth_user)
    }

    @PostMapping("/telegram")
    @Authorized(googleIdToken = false)
    fun recordTelegramEntry(@RequestBody telegramEntry: TelegramEntry, @RequestAttribute("auth_user") auth_user: User?): Entry {
        return entryService.recordTelegramEntry(telegramEntry, auth_user)
    }

    @PostMapping("/bulk")
    @Authorized
    fun recordEntries(@RequestBody entries: List<Entry>, @RequestAttribute("auth_user") auth_user: User?): List<Entry> {
        return entryService.recordEntries(entries, auth_user)
    }

    @PostMapping("/bulk/telegram")
    @Authorized(googleIdToken = false)
    fun recordTelegramEntries(@RequestBody telegramEntries: List<TelegramEntry>, @RequestAttribute("auth_user") auth_user: User?): List<Entry> {
        return entryService.recordTelegramEntries(telegramEntries, auth_user)
    }

    /**
     * Get a list of entries for the specified user
     *
     * Use the fromDate and toDate request parameters to set the range of entries to select. Exclude the toDate to get
     * entries up to the current date. Exclude both to get all entries for the user.
     *
     * @param userId user id
     * @param fromDate get entries from the specified date (optional)
     * @param toDate get entries up to and including the specified date (optional)
     * @return a list of entries
     */
    @GetMapping("/users")
    @Transactional(readOnly = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun getEntriesByUser(
        @RequestParam("user_id") userId: String,
        @RequestParam("from_date", required = false) fromDate: String?,
        @RequestParam("to_date", required = false) toDate: String?
    ): ResponseEntity<List<Entry>> {
        return ResponseEntity.ok(entryService.getEntries(userId, fromDate, toDate))
    }

    @GetMapping("/users/telegram")
    @Transactional(readOnly = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun getEntriesByTelegramUser(
            @RequestParam("telegram_id") telegramId: String,
            @RequestParam("from_date", required = false) fromDate: String?,
            @RequestParam("to_date", required = false) toDate: String?
    ): ResponseEntity<List<Entry>> {
        return ResponseEntity.ok(entryService.getEntriesByTelegramId(telegramId, fromDate, toDate))
    }


    @GetMapping("/groups")
    @Transactional(readOnly = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun getEntriesByGroup(
        @RequestParam("group_id") groupId: String,
        @RequestParam("from_date", required = false) fromDate: String?,
        @RequestParam("to_date", required = false) toDate: String?
    ): ResponseEntity<List<Entry>> {
        return ResponseEntity.ok(entryService.getEntriesByGroupAndDate(groupId, fromDate, toDate))
    }
}