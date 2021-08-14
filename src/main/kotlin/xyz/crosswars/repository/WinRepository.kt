package xyz.crosswars.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import xyz.crosswars.entities.Win
import xyz.crosswars.entities.WinId

@Repository
interface WinRepository : CrudRepository<Win, WinId> {

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
}