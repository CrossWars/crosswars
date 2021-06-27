package xyz.crossward.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "WINS")
class Wins(
    @Id @Column(name = "user_id") var userId: String,
    @Id @Column(name = "group_id") var groupId: String,
    @Column var wins: Int
) : Serializable