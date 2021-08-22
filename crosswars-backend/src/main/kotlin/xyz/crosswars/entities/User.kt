package xyz.crosswars.entities

import javax.persistence.*

@Entity
@Table(
    name = "USER",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["email"])
    ]
)
data class User(
    @Id @Column(name = "user_id") val userId: String, // telegram ID for telegram users, email for website users
    @Column val name: String,
    @Column val remind: Boolean?,
    @Column val email: String?
)