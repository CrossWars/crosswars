package xyz.crossward.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import xyz.crossward.entities.Group
import java.util.stream.Stream

@Repository
interface GroupRepository : CrudRepository<Group, String> {

    @Query("select g from Group g where g.name = :name")
    fun findGroupByName(name: String): Stream<Group>
}