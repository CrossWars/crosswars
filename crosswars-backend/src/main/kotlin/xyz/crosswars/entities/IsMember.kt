package xyz.crosswars.entities

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "IS_MEMBER")
@IdClass(IsMemberId::class)
data class IsMember(
    @Id @Column(name = "user_id") val userId: String,
    @Id @Column(name = "group_id") val groupId: String
) : Serializable

data class IsMemberId(
    val userId: String = "",
    val groupId: String = ""
) : Serializable