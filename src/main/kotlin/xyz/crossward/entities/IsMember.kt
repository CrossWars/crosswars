package xyz.crossward.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "IS_MEMBER")
data class IsMember(
    @Id @Column(name = "user_id") val userId: String,
    @Id @Column(name = "group_id") val groupId: String
) : Serializable