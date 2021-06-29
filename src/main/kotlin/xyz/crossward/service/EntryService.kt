package xyz.crossward.service

import org.springframework.stereotype.Service
import xyz.crossward.entities.Entry
import xyz.crossward.exception.BadRequestException
import xyz.crossward.repository.EntryRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.*

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
        val puzzleDate: LocalDate = if (entry.date != null) {
            if (isValidPuzzleDate(entry.date))
                entry.date
            else
                throw BadRequestException(
                    "Date ${entry.date} is not a valid puzzle date. " +
                            "Time in New York: ${ZonedDateTime.now(ZoneId.of("US/Eastern"))}"
                )
        } else {
            // use current puzzle date
            getPuzzleDate(ZonedDateTime.now(ZoneId.of("US/Eastern")))
        }

        val savedEntry = Entry(
            userId = entry.userId,
            time = entry.time,
            date = puzzleDate
        )

        entryRepository.save(savedEntry)
        return savedEntry
    }

    /**
     * Valid dates are any date before or on the current puzzle date. Nothing in the future is allowed
     */
    private fun isValidPuzzleDate(date: LocalDate): Boolean {
        val currentPuzzleDate = getPuzzleDate(ZonedDateTime.now(ZoneId.of("US/Eastern")))
        return !date.isAfter(currentPuzzleDate)
    }

    /**
     * Get the current NYT puzzle date.
     *
     * Crosswords are available at the following times (in EST):
     * Monday: 6PM (Sunday) - 10PM (Monday)
     * Tuesday - Friday: 10PM (previous day) - 10PM
     * Saturday: 10PM (Friday) - 6PM
     * Sunday: 6PM (Saturday) - 6PM
     */
    fun getPuzzleDate(currentDate: ZonedDateTime): LocalDate =
        when (currentDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)) {
            in listOf("Mon", "Tue", "Wed", "Thu", "Fri") ->
                if (currentDate.toLocalTime().isAfter(LocalTime.parse("22:00:00"))) {
                    currentDate.plusDays(1).toLocalDate()
                } else {
                    currentDate.toLocalDate()
                }
            in listOf("Sat", "Sun") ->
                if (currentDate.toLocalTime().isAfter(LocalTime.parse("18:00:00"))) {
                    currentDate.plusDays(1).toLocalDate()
                } else {
                    currentDate.toLocalDate()
                }
            else ->
                // Return the current date if for whatever reason the code gets here
                currentDate.toLocalDate()
        }
}
