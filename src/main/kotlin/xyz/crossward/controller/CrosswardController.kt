package xyz.crossward.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import xyz.crossward.entities.Player
import xyz.crossward.service.PlayerService

@RestController
@RequestMapping("/api")
class CrosswardController(
    var service: PlayerService
) {

    @PostMapping("/players")
    @ResponseStatus(HttpStatus.CREATED)
    fun createPlayer(@RequestBody player: Player): Player {
        return service.createPlayer(player)
    }

    @GetMapping("/players/emails/{player_email}")
    @ResponseStatus(HttpStatus.OK)
    fun getPlayerById(@PathVariable("player_email") playerEmail: String): ResponseEntity<Player> {
        val player = service.findPlayerByEmail(playerEmail)
        return ResponseEntity.ok(player)
    }

    @GetMapping("players/names/{player_name}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    fun getPlayerByName(@PathVariable("player_name") playerName: String): ResponseEntity<Player> {
        val player = service.findPlayerByName(playerName)
        return ResponseEntity.ok(player)
    }
}