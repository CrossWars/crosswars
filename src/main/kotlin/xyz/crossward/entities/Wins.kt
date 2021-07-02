package xyz.crossward.entities

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "WINS")
@IdClass(WinsId::class)
data class Wins(
    @Id @Column(name = "user_id") var userId: String,
    @Id @Column(name = "group_id") var groupId: String,
    @Column var wins: Int
) : Serializable

class WinsId(
    val userId: String = "",
    val groupId: String = ""
) : Serializable