package xyz.crossward.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import xyz.crossward.entities.Group
import xyz.crossward.entities.User
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

    @GetMapping("/ids")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getGroupById(@RequestParam("group_id", required = true) group_id: String,
    ): ResponseEntity<Group> {
        return ResponseEntity.ok(service.findGroupById(group_id))
    }


    @GetMapping("/names")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getGroupByName(@RequestParam("group_name", required = true) name: String): ResponseEntity<Group> { return ResponseEntity.ok(service.findGroupByName(name))
    }


    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getUsersByGroupId(@RequestParam("group_id", required = true) group_id: String): ResponseEntity<List<User>> {
        return ResponseEntity.ok(service.findUsersByGroupId(group_id))
    }


}