package xyz.crossward.entities

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "IS_MEMBER")
class IsMember(
    @Id @Column(name = "user_id") val userId: Int,
    @Id @Column(name = "group_id") val groupId: Int
): Serializable {
}