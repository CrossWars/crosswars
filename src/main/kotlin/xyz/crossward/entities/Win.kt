package xyz.crossward.entities

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "WINS")
@IdClass(WinId::class)
data class Win(
    @Id @Column(name = "user_id") var userId: String,
    @Id @Column(name = "group_id") var groupId: String,
    @Column var wins: Int
) : Serializable

class WinId(
    val userId: String,
    val groupId: String
) : Serializable