package xyz.crossward.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import xyz.crossward.entities.User
import java.util.stream.Stream

@Repository
interface UserRepository : CrudRepository<User, Int> {

    @Query("select * from USER u where u.name = :name")
    fun findUserByName(name: String): Stream<User>

    @Query("select u from User u where u.email = :email")
    fun findUserByEmail(email: String): Stream<User>
}