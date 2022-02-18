package xyz.crosswars.service

import org.springframework.stereotype.Service
import xyz.crosswars.entities.Group
import xyz.crosswars.entities.IsMember
import xyz.crosswars.entities.IsMemberId
import xyz.crosswars.entities.User
import xyz.crosswars.exception.BadRequestException
import xyz.crosswars.exception.NoContentException
import xyz.crosswars.repository.GroupRepository
import xyz.crosswars.repository.IsMemberRepository
import xyz.crosswars.repository.UserRepository
import xyz.crosswars.util.unwrap
import java.util.*

//match with alphanumeric, separated by space between words
private const val VALID_GROUP_NAME_PATTERN = "^[a-zA-Z1-9]+( ?[a-zA-Z1-9])*\$"
private const val MAX_CREATED_GROUPS_PER_USER = 5

@Service
class GroupService(
    val groupRepository: GroupRepository,
    val userRepository: UserRepository,
    val isMemberRepository: IsMemberRepository
) {


    fun findGroupById(groupId: String): Group {
        return groupRepository.findById(groupId).unwrap()
            ?: throw NoContentException("Could not find group with id $groupId")
    }

    fun findGroupByName(name: String): Group {
        return groupRepository.findGroupByName(name.lowercase()).findFirst().unwrap()
            ?: throw NoContentException("Could not find group with name $name")
    }

    fun findGroupsByUser(userId: String): List<Group> {
        return groupRepository.findGroupsByUser(userId).toList()
    }

    fun findAllGroups(): List<Group> {
        return groupRepository.findAllGroups().toList()
    }
    /**
     * Finds all users in a given group
     *
     * @param groupId the id of the group to get members from
     * @return A list of the users that are members of the given group
     */
    fun findUsersByGroupId(groupId: String): List<User> {
        if (!groupRepository.existsById(groupId)) {
            throw BadRequestException("A group with ID $groupId does not exist")
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
        if (groupRepository.existsById(group.groupId)) {
            throw BadRequestException("A group with ID ${group.groupId} already exists")
        }
        // create a telegram group
        val savedGroup = Group(
            groupId = group.groupId,
            name = group.name.lowercase(),
            createdBy = null
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
    fun createWebsiteGroup(group: Group, createdByUser: User): Group {
        if (!isGroupNameValid(group.name)) {
            throw BadRequestException("The name \"${group.name}\" is invalid.")
        }
        if (groupRepository.getCreatedGroupsCountByUserId(createdByUser.userId) >= MAX_CREATED_GROUPS_PER_USER) {
            throw BadRequestException("Max number of created groups reached")
        }
        if (groupRepository.existsGroupByNameIgnoreCase(group.name)) {
            throw BadRequestException("A group with name \"${group.name}\" already exists")
        }
        val groupId = createUniqueGroupId()
        // create a website group
        val savedGroup = Group(
            groupId = groupId,
            name = group.name,
            createdBy = createdByUser.userId
        )
        groupRepository.save(savedGroup)
        return savedGroup
    }

    /**
     * Adds a User to a Group, given a GroupId and UserId
     *
     * @param groupId The groupId of the group to add the user to
     * @param userId The userId of the user to add
     * @return the created IsMember relation
     */
    fun addUserToGroup(groupId: String, userId: String): IsMember {
        if (!userRepository.existsById(userId)) {
            throw BadRequestException("A user with ID $userId does not exist")
        }
        if (!groupRepository.existsById(groupId)) {
            throw BadRequestException("A group with ID $groupId does not exist")
        }
        if (isMemberRepository.existsById(IsMemberId(userId, groupId))) {
            throw BadRequestException("A user with ID $userId is already a member of a group with ID $groupId")
        }

        val savedIsMember = IsMember(
            userId = userId,
            groupId = groupId
        )
        isMemberRepository.save(savedIsMember)
        return savedIsMember
    }

    private fun isGroupNameValid(groupName: String): Boolean {
        if(groupName.length < 3 || groupName.length > 25) return false
        return VALID_GROUP_NAME_PATTERN.toRegex().matches(groupName)
    }

    private fun createUniqueGroupId(): String {
        val max = 9999999999999L
        val min = 1000000000000L
        var id = (Math.random() * (max - min)).toLong() + min
        while(groupRepository.existsById(id.toString())) {
            id = (Math.random() * (max - min)).toLong() + min
        }
        return id.toString()
    }
}
