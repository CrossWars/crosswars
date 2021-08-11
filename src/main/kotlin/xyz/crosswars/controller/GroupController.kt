package xyz.crosswars.controller

import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import xyz.crosswars.entities.Group
import xyz.crosswars.entities.IsMember
import xyz.crosswars.entities.User
import xyz.crosswars.service.GroupService

@Api(tags = ["Groups"])
@RestController
@RequestMapping("/api/groups")
class GroupController(
    var service: GroupService
) {
    @PostMapping("/telegram")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTelegramGroup(@RequestBody group: Group): Group {
        return service.createTelegramGroup(group)
    }

    @PostMapping("/website")
    @ResponseStatus(HttpStatus.CREATED)
    fun createGroup(@RequestBody group: Group): Group {
        return service.createWebsiteGroup(group)
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    fun addUserToGroup(
        @RequestParam("group_id", required = true) groupId: String,
        @RequestParam("user_id", required = true) userId: String
    ): IsMember {
        return service.addUserToGroup(groupId, userId)
    }

    @GetMapping("/ids")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getGroupById(
        @RequestParam("group_id", required = true) groupId: String,
    ): ResponseEntity<Group> {
        return ResponseEntity.ok(service.findGroupById(groupId))
    }

    @GetMapping("/names")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getGroupByName(@RequestParam("group_name", required = true) groupName: String): ResponseEntity<Group> {
        return ResponseEntity.ok(service.findGroupByName(groupName))
    }

    /**
     * Get a list of all the groups a user is a member of
     *
     * @param userId
     * @return a list of groups
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getGroupsByUser(@RequestParam("user_id", required = true) userId: String): ResponseEntity<List<Group>> {
        return ResponseEntity.ok(service.findGroupsByUser(userId))
    }

    @GetMapping("/users/groups")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getUsersByGroupId(@RequestParam("group_id", required = true) groupId: String): ResponseEntity<List<User>> {
        return ResponseEntity.ok(service.findUsersByGroupId(groupId))
    }
}