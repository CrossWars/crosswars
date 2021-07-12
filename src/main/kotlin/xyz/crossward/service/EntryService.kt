package xyz.crossward.service

import org.springframework.stereotype.Service
import xyz.crossward.entities.Entry
import xyz.crossward.entities.entryDateString
import xyz.crossward.entities.localDate
import xyz.crossward.exception.BadRequestException
import xyz.crossward.repository.EntryRepository
import xyz.crossward.util.currentDateInEST
import java.time.LocalDate
import java.time.LocalTime
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
        val puzzleDate: LocalDate? = entry.localDate()?.let {
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
            date = puzzleDate?.entryDateString()
        )

        entryRepository.save(savedEntry)
        return savedEntry
    }

    /**
     * Valid dates are any date before or on the current puzzle date. Nothing in the future is allowed
     */
    private fun isValidPuzzleDate(date: LocalDate): Boolean =
        !date.isAfter(getPuzzleDate(currentDateInEST()))

    /**
     * Get the current NYT puzzle date.
     *
     * Crosswords are available at the following times (in EST):
     * Monday: 6PM (Sunday) - 10PM (Monday)
     * Tuesday - Friday: 10PM (previous day) - 10PM
     * Saturday: 10PM (Friday) - 6PM
     * Sunday: 6PM (Saturday) - 6PM
     */
    fun getPuzzleDate(date: ZonedDateTime): LocalDate =
        when (date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)) {
            in listOf("Mon", "Tue", "Wed", "Thu", "Fri") ->
                if (date.toLocalTime().isAfter(LocalTime.parse("22:00:00"))) {
                    date.plusDays(1).toLocalDate()
                } else {
                    date.toLocalDate()
                }
            in listOf("Sat", "Sun") ->
                if (date.toLocalTime().isAfter(LocalTime.parse("18:00:00"))) {
                    date.plusDays(1).toLocalDate()
                } else {
                    date.toLocalDate()
                }
            else ->
                // Return the current date if for whatever reason the code gets here
                date.toLocalDate()
        }

}
