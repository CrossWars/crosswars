package xyz.crossward.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import xyz.crossward.entities.User
import xyz.crossward.service.UserService

@RestController
@RequestMapping("/api")
class UserController(
    var service: UserService
) {

    @PostMapping("/users/telegram")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTelegramUser(@RequestBody user: User): User {
        return service.createTelegramUser(user)
    }

    @PostMapping("/users/website")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody user: User): User {
        return service.createWebsiteUser(user)
    }

    @GetMapping("/users/ids/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getUserById(@PathVariable("id") id: String): ResponseEntity<User> {
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