package xyz.crossward.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import xyz.crossward.entities.Entry
import xyz.crossward.entities.EntryId
import java.time.LocalDate
import java.util.stream.Stream

@Repository
interface EntryRepository : CrudRepository<Entry, EntryId> {

    @Query("select count(e) from Entry e where e.userId = :userId and e.date = :date")
    fun count(userId: String, date: LocalDate?): Long

    @Query("select e.time from Entry e where e.userId = :userId")
    fun getTimesByUserId(userId: String): Stream<Int>

    @Query(
        """
        select e from Entry e
        where e.date between :fromDate and :toDate and e.userId = :userId
        order by e.date
    """
    )
    fun getEntriesByDateRange(userId: String, fromDate: String, toDate: String): Stream<Entry>

    @Query("select e from Entry e where e.userId = :userId order by e.date")
    fun getAllEntries(userId: String): Stream<Entry>
}
