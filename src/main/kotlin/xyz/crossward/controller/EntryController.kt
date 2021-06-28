package xyz.crossward.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xyz.crossward.entities.Entry
import xyz.crossward.service.EntryService

@RestController
@RequestMapping("/api")
class EntryController(
    private val entryService: EntryService
) {

    @PostMapping("/entries")
    fun recordEntry(@RequestBody entry: Entry): Entry {
        return entryService.recordEntry(entry)
    }
}