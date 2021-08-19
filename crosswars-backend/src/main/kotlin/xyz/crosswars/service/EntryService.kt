package xyz.crosswars.service

import org.springframework.stereotype.Service
import xyz.crosswars.entities.Entry
import xyz.crosswars.entities.entryDateString
import xyz.crosswars.entities.localDate
import xyz.crosswars.exception.BadRequestException
import xyz.crosswars.repository.EntryRepository
import xyz.crosswars.util.currentDateInEST
import xyz.crosswars.util.getPuzzleDate
import xyz.crosswars.util.getPuzzleDateInEST
import xyz.crosswars.util.isValidPuzzleDate
import java.time.LocalDate

@Service
class EntryService(
    private val entryRepository: EntryRepository
) {
    /**
     * Record a new entry into the database.
     *
     * If a date is not specified, then then the result of LocalDate.now() will be used
     *
     * @param entry Entry object to insert into the database
     */
    fun recordEntry(entry: Entry): Entry {
        val puzzleDate: LocalDate = entry.localDate()?.let {
            if (isValidPuzzleDate(it))
                it
            else
                throw BadRequestException(
                    "Date ${entry.date} is not a valid puzzle date. " +
                            "Time in New York: ${currentDateInEST()}"
                )
        } ?: run {
            // use current puzzle date
            getPuzzleDate(currentDateInEST())
        }

        val savedEntry = Entry(
            userId = entry.userId,
            time = entry.time,
            date = puzzleDate.entryDateString()
        )

        entryRepository.save(savedEntry)
        return savedEntry
    }

    fun getEntries(userId: String, fromDate: String?, toDate: String?): List<Entry> =
        // only fromDate is specified
        if (fromDate != null && toDate == null) {
            entryRepository.getEntriesByDateRange(
                userId,
                fromDate,
                getPuzzleDateInEST()
            ).toList()
        }
        // both fromDate and toDate are specified
        else if (fromDate != null && toDate != null) {
            entryRepository.getEntriesByDateRange(userId, fromDate, toDate).toList()
        } else {
            // No date is specified
            entryRepository.getAllEntries(userId).toList()
        }

    fun getEntriesByGroupAndDate(groupId: String, fromDate: String?, toDate: String?): List<Entry> =
        // only fromDate is specified
        if (fromDate != null && toDate == null) {
            entryRepository.getEntriesByGroupAndDate(
                groupId,
                fromDate,
                getPuzzleDateInEST()
            ).toList()
        }
        // both fromDate and toDate are specified
        else if (fromDate != null && toDate != null) {
            entryRepository.getEntriesByGroupAndDate(groupId, fromDate, toDate).toList()
        } else {
            // No date is specified
            val currentPuzzleDate = getPuzzleDateInEST()
            entryRepository.getEntriesByGroupAndDate(groupId, currentPuzzleDate, currentPuzzleDate).toList()
        }
}
