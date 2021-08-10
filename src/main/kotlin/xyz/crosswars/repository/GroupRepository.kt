package xyz.crosswars.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import xyz.crosswars.entities.Group
import xyz.crosswars.entities.User
import java.util.stream.Stream

@Repository
interface GroupRepository : CrudRepository<Group, String> {

    @Query("select g from Group g where g.name = :name")
    fun findGroupByName(name: String): Stream<Group>

    @Query("select u from User u, IsMember m where m.groupId = :group_id and m.userId = u.userId")
    fun findUsersByGroupId(group_id: String): Stream<User>
}