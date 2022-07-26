package xyz.crosswars.service

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import xyz.crosswars.entities.User
import xyz.crosswars.exception.BadRequestException
import xyz.crosswars.exception.NoContentException
import xyz.crosswars.repository.UserRepository
import xyz.crosswars.util.unwrap
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

    fun findUserById(id: String?, auth_user: User?): User =
            if (auth_user != null && (id == null || auth_user.userId == id)) {
                auth_user.apply { this.email = null }
            } else if (id !== null){
                // exclude emails of other users
                userRepository.findById(id).unwrap()?.apply { this.email = null }
                        ?: throw NoContentException("Could not find user with id $id")
            }
            else {
                throw BadRequestException("User ID must be provided") //if using auth token, must include user id
            }

    @Transactional(readOnly = true)
    fun findUserByTelegramId(telegramId: String): User {
        return userRepository.findUserByTelegramId(telegramId).findFirst().unwrap()
                ?: throw BadRequestException("Telegram ID does not correspond to a CrossWars user")

    }
    fun findUserByEmail(email: String): User =
        userRepository.findUserByEmail(email.lowercase()).findFirst().unwrap()
            ?: throw NoContentException("Could not find user with email $email")

    fun findUserByName(name: String): User =
        userRepository.findUserByName(name.lowercase()).findFirst().unwrap()
            ?: throw NoContentException("Could not find user with name $name")

    /**
     * Creates a telegram user (email is optional)
     *
     * @param user user to create
     * @return user saved to the database
     */
    fun createTelegramUser(user: User): User {
        if (userRepository.existsById(user.userId)) {
            throw BadRequestException("A user with ID ${user.userId} already exists")
        }
        // create a telegram user
        val savedUser = User(
            userId = user.userId,
            name = user.name.lowercase(),
            email = user.email?.lowercase(),
            remind = user.remind ?: false,
            telegramId = user.telegramId
        )
        userRepository.save(savedUser)
        return savedUser
    }

    /**
     * Creates a website user (ID is assumed to be the email)
     *
     * @param user user to create
     * @return user saved to the database
     */
    fun createWebsiteUser(user: User): User {
        if (userRepository.existsById(user.userId)) {
            throw BadRequestException("A user with ID ${user.userId} already exists")
        }
        val savedUser = User(
            userId = user.userId.lowercase(),
            name = user.name,
            email = user.email!!.lowercase(),
            remind = user.remind ?: false,
            telegramId = null
        )
        userRepository.save(savedUser)
        return savedUser
    }

    fun userExists(userId: String): Boolean =
        userRepository.existsById(userId)
}
