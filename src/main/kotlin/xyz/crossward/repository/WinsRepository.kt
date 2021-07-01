package xyz.crossward.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import xyz.crossward.entities.Win
import xyz.crossward.entities.WinId
import java.util.stream.Stream

@Repository
interface WinsRepository : CrudRepository<Win, WinId> {

    @Query("select w from Win w where w.userId = :userId and w.groupId = :groupId")
    fun findByUserAndGroup(userId: String, groupId: String): Stream<Win>
}