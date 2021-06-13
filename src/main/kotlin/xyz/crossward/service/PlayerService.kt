package xyz.crossward.service

import org.springframework.stereotype.Service
import xyz.crossward.entities.Player
import xyz.crossward.exception.NoContentException
import xyz.crossward.repository.PlayerRepository
import java.util.*
import javax.sql.DataSource

@Service
class PlayerService(
    var dataSource: DataSource,
    val playerRepository: PlayerRepository
) {

    // Example of executing a statement directly with the JDBC driver
    fun select(): String {
        val sql = "SELECT id, name FROM player where id = 1"
        val stmt = dataSource.connection.createStatement()

        val result = stmt.executeQuery(sql)
        while (result.next()) {
            val id = result.getInt(1)
            val name = result.getString(2)

            return "Retrieved $name from database with id $id"
        }

        return "nothing found"
    }

    fun findPlayerByEmail(email: String): Player {
        return playerRepository.findById(email.lowercase()).unwrap()
            ?: throw NoContentException("Could not find player with email $email")
    }

    fun findPlayerByName(playerName: String): Player {
        return playerRepository.findPlayerByName(playerName.lowercase()).findFirst().unwrap()
            ?: throw NoContentException("Could not find player with name $playerName")
    }

    fun createPlayer(player: Player): Player {

        // lower case the text fields
        val savedPlayer = Player(
            email = player.email.lowercase(),
            name = player.name.lowercase(),
            telegramId = player.telegramId
        )
        playerRepository.save(savedPlayer)
        return savedPlayer
    }
}

// Convert java Optional into kotlin null safety
fun <T> Optional<T>.unwrap(): T? = orElse(null)
