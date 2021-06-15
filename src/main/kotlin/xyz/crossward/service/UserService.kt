package xyz.crossward.service

import org.springframework.stereotype.Service
import xyz.crossward.entities.User
import xyz.crossward.exception.NoContentException
import xyz.crossward.repository.UserRepository
import java.util.*
import javax.sql.DataSource

@Service
class UserService(
    var dataSource: DataSource,
    val userRepository: UserRepository
) {

    // Example of executing a statement directly with the JDBC driver
    fun select(): String {
        val sql = "SELECT id, name FROM user where id = 1"
        val stmt = dataSource.connection.createStatement()

        val result = stmt.executeQuery(sql)
        while (result.next()) {
            val id = result.getInt(1)
            val name = result.getString(2)

            return "Retrieved $name from database with id $id"
        }

        return "nothing found"
    }

    fun findUserById(id: Int): User {
        return userRepository.findById(id).unwrap()
            ?: throw NoContentException("Could not find user with id $id")
    }

    fun findUserByEmail(email: String): User {
        return userRepository.findUserByEmail(email.lowercase()).findFirst().unwrap()
            ?: throw NoContentException("Could not find user with email $email")
    }

    fun findUserByName(name: String): User {
        return userRepository.findUserByName(name.lowercase()).findFirst().unwrap()
            ?: throw NoContentException("Could not find user with name $name")
    }

    fun createUser(user: User): User {
        // lower case the text fields
        val savedUser = User(
            email = user.email.lowercase(),
            name = user.name.lowercase(),
            userId = user.userId,
            remind = user.remind ?: false
        )
        userRepository.save(savedUser)
        return savedUser
    }
}

// Convert java Optional into kotlin null safety
fun <T> Optional<T>.unwrap(): T? = orElse(null)
