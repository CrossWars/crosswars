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
@Table(name = "friends")
class FriendShip(
    @Id @Column(name = "friend_1_email")
    val friend1Email: String,

    @Id @Column(name = "friend_2_email")
    val friend2Email: String
): Serializable {
}