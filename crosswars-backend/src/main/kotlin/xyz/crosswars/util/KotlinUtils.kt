package xyz.crosswars.util

import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

// Convert java Optional into kotlin null safety
fun <T> Optional<T>.unwrap(): T? = orElse(null)

fun currentDateInEST(): ZonedDateTime = ZonedDateTime.now(ZoneId.of("US/Eastern"))