package xyz.crosswars.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import xyz.crosswars.entities.Wins
import xyz.crosswars.entities.WinsId

@Repository
interface WinsRepository : CrudRepository<Wins, WinsId> {

    @Modifying
    @Transactional
    @Query(
        """
        update Wins w 
        set w.wins = w.wins + 1 
        where w.userId = :userId and w.groupId = :groupId
        """
    )
    fun addWin(userId: String, groupId: String)
}