package xyz.crosswars.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import xyz.crosswars.entities.Group
import xyz.crosswars.entities.User
import xyz.crosswars.exception.BadRequestException
import java.util.stream.Stream

@Repository
interface GroupRepository : CrudRepository<Group, String> {

    @Query("select g from Group g where g.name = :name")
    fun findGroupByName(name: String): Stream<Group>

    @Query("select u from User u, IsMember im where im.groupId = :groupId and im.userId = u.userId")
    fun findUsersByGroupId(groupId: String): Stream<User>

    @Query("select g from Group g, IsMember im where im.userId = :userId and g.id = im.groupId")
    fun findGroupsByUser(userId: String): Stream<Group>

    @Query("select g from Group g")
    fun findAllGroups(): Stream<Group>

    @Query("select count(g) from Group g where g.createdBy = :userId")
    fun getCreatedGroupsCountByUserId(userId: String): Int

    //the following is a derived Spring query
    fun existsGroupByNameIgnoreCase(name: String): Boolean
}

fun GroupRepository.checkIfGroupExists(groupId: String) {
    if (!this.existsById(groupId)) {
        throw BadRequestException("group_id $groupId does not exist")
    }
}