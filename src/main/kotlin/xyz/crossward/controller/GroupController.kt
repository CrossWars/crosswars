package xyz.crossward.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import xyz.crossward.entities.Group
import xyz.crossward.service.GroupService
import java.net.URLDecoder

@RestController
@RequestMapping("/api")
class GroupController(
        var service: GroupService
) {

    @PostMapping("/groups/telegram")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTelegramGroup(@RequestBody group: Group): Group {
        return service.createTelegramGroup(group)
    }

    @PostMapping("/groups/website")
    @ResponseStatus(HttpStatus.CREATED)
    fun createGroup(@RequestBody group: Group): Group {
        return service.createWebsiteGroup(group)
    }

    @GetMapping("/groups/ids/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getGroupById(@PathVariable("id") id: String): ResponseEntity<Group> {

        return ResponseEntity.ok(service.findGroupById(id))
    }


    @GetMapping("/groups/names/{name}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getGroupByName(@PathVariable("name") name: String): ResponseEntity<Group
            > {
        //var decoded = URLDecoder.decode(name, "UTF-8");
        return ResponseEntity.ok(service.findGroupByName(name))
    }




}