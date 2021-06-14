package xyz.crossward.entities

import java.io.Serializable
import javax.persistence.*

/**
 * Must implement Serializable for composite keys to work
 */
@Entity
@Table(name = "IS_FRIENDS")
class IsFriends(
    @Id @Column(name = "user_id_1")
    val userId1: String,

    @Id @Column(name = "user_id_2")
    val userId2: String
): Serializable {
}