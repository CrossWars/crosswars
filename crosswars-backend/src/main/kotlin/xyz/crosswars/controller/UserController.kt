package xyz.crosswars.controller

import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import xyz.crosswars.config.Authorized
import xyz.crosswars.config.AuthorizedNewUser
import xyz.crosswars.entities.User
import xyz.crosswars.service.UserService

@RestController
@RequestMapping("/users")
@Api(tags = ["Users"])
class UserController(
    var service: UserService
) {

    @PostMapping("/telegram")
    @ResponseStatus(HttpStatus.CREATED)
    @Authorized(googleIdToken = false)
    fun createTelegramUser(@RequestBody user: User): User {
        return service.createTelegramUser(user)
    }

    @PostMapping("/website")
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizedNewUser
    fun createUser(@RequestAttribute("auth_user") user: User): User {
        return service.createWebsiteUser(user)
    }

    //If ID is null, return auth user
    @GetMapping("/ids")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    @Authorized
    fun getUserById(
        @RequestParam("user_id", required = false) id: String?,
        @RequestAttribute("auth_user") auth_user: User?
    ): ResponseEntity<User> {
        return ResponseEntity.ok(service.findUserById(id, auth_user))
    }

    @GetMapping("/ids/telegram")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    @Authorized
    fun getUserByTelegramId(
            @RequestParam("telegram_id") id: String
    ): ResponseEntity<User> {
        return ResponseEntity.ok(service.findUserByTelegramId(id).apply { this.email = null })
    }

    @GetMapping("/emails")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    @Authorized
    fun getUserByEmail(
        @RequestParam("user_email") email: String,
        @RequestAttribute("auth_user") user: User?
    ): ResponseEntity<User> {
        email.let {
            return ResponseEntity.ok(service.findUserByEmail(email))
        }
    }

    @GetMapping("/names")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getUserByName(
        @RequestParam("user_name") name: String,
        @RequestAttribute("auth_user") user: User?
    ): ResponseEntity<User> {
        name.let {
            return ResponseEntity.ok(service.findUserByName(name))
        }
    }
}