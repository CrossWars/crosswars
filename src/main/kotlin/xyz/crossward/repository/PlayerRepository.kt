package xyz.crossward.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import xyz.crossward.entities.Player
import java.util.stream.Stream

@Repository
interface PlayerRepository : CrudRepository<Player, Int> {

    @Query("select p from Player p where name = ?1")
    fun findPlayerByName(name: String): Stream<Player>
}