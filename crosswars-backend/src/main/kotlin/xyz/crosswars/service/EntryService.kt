package xyz.crosswars.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import xyz.crosswars.entities.*
import xyz.crosswars.exception.BadRequestException
import xyz.crosswars.repository.EntryRepository
import xyz.crosswars.repository.UserRepository
import xyz.crosswars.util.currentDateInEST
import xyz.crosswars.util.getPuzzleDate
import xyz.crosswars.util.getPuzzleDateInEST
import xyz.crosswars.util.isValidPuzzleDate
import java.security.KeyStore
import java.time.LocalDate
import java.util.stream.Stream

@Service
class EntryService(
    private val entryRepository: EntryRepository,
    private val userService: UserService,
    private val userRepository: UserRepository

) {
    /**
     * Record a new entry into the database.
     *
     * If a date is not specified, then then the result of LocalDate.now() will be used
     *
     * @param entry Entry object to insert into the database
     * @param auth_user The authenticated user recording this entry. If null, using internal authentication
     */
    fun recordEntry(entry: Entry, auth_user: User?): Entry {
        if(auth_user != null && auth_user.userId != entry.userId){
            throw ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Auth user does not match entry user")
        }
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
    /**
     * Record a list of new entries into the database.
     *
     * If a date is not specified, then then the result of LocalDate.now() will be used
     *
     * @param entries List of entry objects to insert into the database
     * @param auth_user The authenticated user recording this entry. If null, using internal authentication
     */
    fun recordEntries(entries: List<Entry>, auth_user: User?): List<Entry> {
        val savedEntries = mutableListOf<Entry>()
        entries.forEach { entry ->
            if(auth_user != null && entry.userId != auth_user.userId){
                throw ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Auth user does not match entry user for all entries")
            }
            val puzzleDate: LocalDate = entry.localDate()?.let {
                if (isValidPuzzleDate(it))
                    it
                else
                    return@forEach // Don't record entries with invalid puzzle dates
            } ?: run {
                // use current puzzle date
                getPuzzleDate(currentDateInEST())
            }
            if(!userRepository.existsById(entry.userId))
            {
                throw BadRequestException(
                        "User ID ${entry.userId} does not exist"
                )
            }
            val savedEntry = Entry(
                    userId = entry.userId,
                    time = entry.time,
                    date = puzzleDate.entryDateString()
            )
            savedEntries.add(savedEntry)
        }
        entryRepository.saveAll(savedEntries)
        return savedEntries
    }

    fun recordTelegramEntry(telegramEntry: TelegramEntry, auth_user: User?): Entry
    {
        val matchedUser = userService.findUserByTelegramId(telegramEntry.telegramId)
        val entry = Entry(
                userId = matchedUser.userId,
                time = telegramEntry.time,
                date = telegramEntry.date
        )
        return recordEntry(entry, auth_user)
    }
    fun recordTelegramEntries(telegramEntries: List<TelegramEntry>, auth_user: User?): List<Entry>
    {
        val entries = mutableListOf<Entry>()
        telegramEntries.forEach { telegramEntry ->
            val entry = Entry(
                    userId = userService.findUserByTelegramId(telegramEntry.telegramId).userId,
                    date = telegramEntry.date,
                    time = telegramEntry.time
            )
            entries.add(entry)
        }
        return recordEntries(entries, auth_user)
    }

    fun getEntriesByTelegramId(telegramId: String, fromDate: String?, toDate: String?): List<Entry>
    {
        val matchedUser = userService.findUserByTelegramId(telegramId)
        return getEntries(matchedUser.userId, fromDate, toDate)
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

    fun getAllEntriesByGroup(groupId: String): Stream<Entry> =
            entryRepository.getAllEntriesByGroup(groupId)
}
