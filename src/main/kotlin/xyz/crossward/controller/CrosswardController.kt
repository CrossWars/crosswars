package xyz.crossward.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import xyz.crossward.entities.User
import xyz.crossward.service.UserService

@RestController
@RequestMapping("/api")
class CrosswardController(
    var service: UserService
) {

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody user: User): User {
        return service.createUser(user)
    }

    @GetMapping("/users/ids/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getUserById(@PathVariable("id") id: Int): ResponseEntity<User> {
        return ResponseEntity.ok(service.findUserById(id))
    }

    @GetMapping("/users/emails/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun getUserByEmail(@PathVariable("email") email: String): ResponseEntity<User> {
        return ResponseEntity.ok(service.findUserByEmail(email))
    }

    @GetMapping("users/names/{name}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getUserByName(@PathVariable("name") name: String): ResponseEntity<User> {
        return ResponseEntity.ok(service.findUserByName(name))
    }
}