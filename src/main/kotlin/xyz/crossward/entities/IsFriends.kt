package xyz.crossward.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * Must implement Serializable for composite keys to work
 */
@Entity
@Table(name = "IS_FRIENDS")
data class IsFriends(
    @Id @Column(name = "user_id_1")
    val userId1: String,

    @Id @Column(name = "user_id_2")
    val userId2: String
) : Serializable