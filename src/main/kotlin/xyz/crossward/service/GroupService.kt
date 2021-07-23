package xyz.crossward.service

import org.springframework.stereotype.Service
import xyz.crossward.entities.Group
import xyz.crossward.entities.IsMember
import xyz.crossward.entities.IsMemberId
import xyz.crossward.entities.User
import xyz.crossward.exception.BadRequestException
import xyz.crossward.exception.NoContentException
import xyz.crossward.repository.GroupRepository
import xyz.crossward.repository.IsMemberRepository
import xyz.crossward.repository.UserRepository
import xyz.crossward.util.unwrap
import java.util.*

@Service
class GroupService(
    val groupRepository: GroupRepository,
    val userRepository: UserRepository,
    val isMemberRepository: IsMemberRepository
) {
    fun findGroupById(group_id: String): Group {
        return groupRepository.findById(group_id).unwrap()
            ?: throw NoContentException("Could not find group with id $group_id")
    }

    fun findGroupByName(name: String): Group {
        return groupRepository.findGroupByName(name.lowercase()).findFirst().unwrap()
            ?: throw NoContentException("Could not find group with name $name")
    }

    /**
     * Finds all users in a given group
     *
     * @param groupId the id of the group to get members from
     * @return A list of the users that are members of the given group
     */
    fun findUsersByGroupId(groupId: String): List<User> {
        if (!groupRepository.existsById(groupId)) {
            throw BadRequestException("A group with ID ${groupId} does not exist")
        }
        return groupRepository.findUsersByGroupId(groupId).toList()
    }

    /**
     * Creates a Telegram group
     *
     * @param group group to create
     * @return group saved to the database
     */
    fun createTelegramGroup(group: Group): Group {
        if (groupRepository.existsById(group.id)) {
            throw BadRequestException("A group with ID ${group.id} already exists")
        }
        // create a telegram group
        val savedGroup = Group(
            id = group.id,
            name = group.name.lowercase()
        )
        groupRepository.save(savedGroup)
        return savedGroup
    }

    /**
     * Creates a Website group, using name as ID
     *
     * @param group group to create
     * @return group saved to the database
     */
    fun createWebsiteGroup(group: Group): Group {
        if (groupRepository.existsById(group.name.lowercase())) {
            throw BadRequestException("A group with ID ${group.name.lowercase()} already exists")
        }
        // create a website group
        val savedGroup = Group(
            id = group.name.lowercase(),
            name = group.name.lowercase()
        )
        groupRepository.save(savedGroup)
        return savedGroup
    }

    /**
     * Adds a User to a Group, given a GroupId and UserId
     *
     * @param groupId The groupId of the group to add the user to
     * @param userID The userId of the user to add
     * @return the created IsMember relation
     */
    fun addUserToGroup(groupId: String, userId: String): IsMember {
        if (!userRepository.existsById(userId)) {
            throw BadRequestException("A user with ID ${userId} does not exist")
        }
        if (!groupRepository.existsById(groupId)) {
            throw BadRequestException("A group with ID ${groupId} does not exist")
        }
        if (isMemberRepository.existsById(IsMemberId(userId, groupId))) {
            throw BadRequestException("A user with ID ${userId} is already a member of a group with ID ${groupId}")
        }

        val savedIsMember = IsMember(
            userId = userId,
            groupId = groupId
        )
        isMemberRepository.save(savedIsMember)
        return savedIsMember
    }
}
