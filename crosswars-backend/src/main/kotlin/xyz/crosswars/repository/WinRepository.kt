package xyz.crosswars.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import xyz.crosswars.entities.Win
import xyz.crosswars.entities.WinCount
import xyz.crosswars.entities.WinId
import java.util.stream.Stream

@Repository
interface WinRepository : CrudRepository<Win, WinId> {

    /**
     * Get the winner in the given group for a specific day
     */
    @Query("select w from Win w where w.groupId = :groupId and w.date = :date")
    fun getWinByGroupId(groupId: String, date: String): Win?

    @Query(
        """
        select count(w) from Win w
        where w.userId = :userId and w.groupId = :groupId
            and w.date between :fromDate and :toDate
        """
    )
    fun getWinCountByDate(userId: String, groupId: String, fromDate: String, toDate: String): Int

    @Query(
        """
        select count(w) from Win w
        where w.userId = :userId and w.groupId = :groupId
        """
    )
    fun getWinCountAllTime(userId: String, groupId: String): Int

    @Query(
        """
        select new xyz.crosswars.entities.WinCount(w.userId, w.groupId, count(w)) 
        from Win w
        where w.groupId = :groupId
        group by w.userId
        """
    )
    fun getWinCountsForAllUsersInGroup(groupId: String): Stream<WinCount>

    @Query("select w from Win w where w.groupId = :groupId")
    fun getWinsForAllUsersInGroup(groupId: String): Stream<Win>

    @Modifying
    fun deleteByGroupId(groupId: String): Long
}