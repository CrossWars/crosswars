package xyz.crosswars.entities

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "WINS")
@IdClass(WinId::class)
data class Win(
    @Id @Column(name = "user_id") var userId: String,
    @Id @Column(name = "group_id") var groupId: String,
    @Id @Column var date: String,
) : Serializable

data class WinId(
    val userId: String = "",
    val groupId: String = "",
    val date: String = ""
) : Serializable

/**
 * Returns win information with account metadata
 */
data class WinCount(
    val userId: String,
    val groupId: String,
    val wins: Int,
    val fromDate: String?,
    val toDate: String?
) {
    constructor(userId: String, groupId: String, wins: Long) : this(userId, groupId, wins.toInt(), null, null)
}

/**
 * Represents a single winner of a group and date
 */
data class Winner(
    val userId: String
)

fun Win.toWinner(): Winner = Winner(this.userId)