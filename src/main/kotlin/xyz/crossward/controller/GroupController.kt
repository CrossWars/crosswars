package xyz.crossward.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import xyz.crossward.entities.Group
import xyz.crossward.entities.User
import xyz.crossward.entities.IsMember
import xyz.crossward.service.GroupService
import java.net.URLDecoder

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

    @PostMapping("/add_user")
    @ResponseStatus(HttpStatus.CREATED)
    fun addUserToGroup(@RequestParam("groupId", required = true) groupId: String,
                        @RequestParam("userId", required = true) userId: String): IsMember{
        return service.addUserToGroup(groupId, userId)
    }

    @GetMapping("/ids")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getGroupById(@RequestParam("groupId", required = true) groupId: String,
    ): ResponseEntity<Group> {
        return ResponseEntity.ok(service.findGroupById(groupId))
    }


    @GetMapping("/names")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getGroupByName(@RequestParam("groupName", required = true) groupName: String): ResponseEntity<Group> { return ResponseEntity.ok(service.findGroupByName(groupName))
    }


    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getUsersByGroupId(@RequestParam("groupId", required = true) groupId: String): ResponseEntity<List<User>> {
        return ResponseEntity.ok(service.findUsersByGroupId(groupId))
    }




}