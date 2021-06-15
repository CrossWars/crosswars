package xyz.crossward.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.persistence.*

@Entity
@Table(
    name = "USER",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["email"])
    ]
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(SnakeCaseStrategy::class)
data class User(
    @Id @Column(name = "user_id") val userId: String, // telegram ID for telegram users, email for website users
    @Column val name: String,
    @Column val remind: Boolean?,
    @Column val email: String
)