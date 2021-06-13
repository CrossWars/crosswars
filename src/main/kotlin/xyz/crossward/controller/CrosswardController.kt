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

    @GetMapping("/players/ids/{player_id}")
    @ResponseStatus(HttpStatus.OK)
    fun getPlayerById(@PathVariable("player_id") playerId: Int): ResponseEntity<Player> {
        val player = service.findPlayerById(playerId)
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