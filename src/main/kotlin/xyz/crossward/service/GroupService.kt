package xyz.crossward.service

import org.springframework.stereotype.Service
import xyz.crossward.entities.Group
import xyz.crossward.exception.BadRequestException
import xyz.crossward.exception.NoContentException
import xyz.crossward.repository.GroupRepository
import java.util.*

@Service
class GroupService(
        val groupRepository: GroupRepository
) {


    fun findGroupById(id: String): Group {
        return groupRepository.findById(id).unwrap()
                ?: throw NoContentException("Could not find group with id $id")
    }

    fun findGroupByName(name: String): Group {
        return groupRepository.findGroupByName(name.lowercase()).findFirst().unwrap()
                ?: throw NoContentException("Could not find group with name $name")
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
}

// Convert java Optional into kotlin null safety
fun <T> Optional<T>.unwrap(): T? = orElse(null)
