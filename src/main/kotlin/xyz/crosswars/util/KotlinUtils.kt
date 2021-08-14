package xyz.crosswars.util

import xyz.crosswars.entities.entryDateString
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.*

// Convert java Optional into kotlin null safety
fun <T> Optional<T>.unwrap(): T? = orElse(null)

fun currentDateInEST(): ZonedDateTime = ZonedDateTime.now(ZoneId.of("US/Eastern"))

/**
 * Get the current puzzle date in EST
 *
 * @return current puzzle date as string
 */
fun getPuzzleDateInEST(): String = getPuzzleDate(currentDateInEST()).entryDateString()

/**
 * Valid dates are any date before or on the current puzzle date. Nothing in the future is allowed
 */
fun isValidPuzzleDate(date: LocalDate): Boolean =
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