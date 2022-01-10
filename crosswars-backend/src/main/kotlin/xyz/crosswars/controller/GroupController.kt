package xyz.crosswars.controller

import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import xyz.crosswars.config.Authorized
import xyz.crosswars.entities.Group
import xyz.crosswars.entities.IsMember
import xyz.crosswars.entities.User
import xyz.crosswars.exception.UnauthorizedException
import xyz.crosswars.service.GroupService
import xyz.crosswars.service.IsMemberService

@Api(tags = ["Groups"])
@RestController
@RequestMapping("/groups")
class GroupController(
    var service: GroupService,
    val isMemberService: IsMemberService
) {
    @PostMapping("/telegram")
    @ResponseStatus(HttpStatus.CREATED)
    @Authorized(googleIdToken = false)
    fun createTelegramGroup(@RequestBody group: Group): Group {
        return service.createTelegramGroup(group)
    }

    @PostMapping("/website")
    @ResponseStatus(HttpStatus.CREATED)
    @Authorized
    fun createGroup(@RequestBody group: Group, @RequestAttribute("auth_user") user: User): Group {
        return service.createWebsiteGroup(group, user)
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Authorized
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
    @Authorized
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
    @Authorized
    fun getGroupsByUser(@RequestAttribute("auth_user") user: User): ResponseEntity<List<Group>> {
        return ResponseEntity.ok(service.findGroupsByUser(user.userId))
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getUsersByGroupId(@RequestParam("group_id", required = true) groupId: String): ResponseEntity<List<User>> {
        return ResponseEntity.ok(service.findUsersByGroupId(groupId))
    }

    @GetMapping("/is_members")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getIsMember(
        @RequestParam("user_id", required = true) userId: String,
        @RequestParam("group_id", required = true) groupId: String
    ): ResponseEntity<Boolean>  {
        return ResponseEntity.ok(isMemberService.getIsMember(userId, groupId))
    }
}