package xyz.crossward.controller

import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import xyz.crossward.entities.Entry
import xyz.crossward.service.EntryService

@RestController
@RequestMapping("/api/entries")
class EntryController(
    private val entryService: EntryService
) {
    @PostMapping
    fun recordEntry(@RequestBody entry: Entry): Entry {
        return entryService.recordEntry(entry)
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
    @GetMapping
    @Transactional(readOnly = true)
    fun getEntries(
        @RequestParam("user_id") userId: String,
        @RequestParam("from_date", required = false) fromDate: String?,
        @RequestParam("to_date", required = false) toDate: String?
    ): ResponseEntity<List<Entry>> {
        return ResponseEntity.ok(entryService.getEntries(userId, fromDate, toDate))
    }
}