package xyz.crossward.service

import org.springframework.stereotype.Service
import xyz.crossward.entities.Entry
import xyz.crossward.exception.BadRequestException
import xyz.crossward.repository.EntryRepository
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
        val savedEntry = Entry(
            userId = entry.userId,
            time = entry.time,
            date = entry.date ?: LocalDate.now()
        )

        if (entryRepository.count(savedEntry.userId, savedEntry.date) > 0) {
            throw BadRequestException("an entry for the date ${savedEntry.date} already exists for user id ${savedEntry.userId}")
        }

        entryRepository.save(savedEntry)
        return savedEntry
    }
}
