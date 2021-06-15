package xyz.crossward.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.persistence.*

@Entity
@Table(name = "WINS")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class Wins(
    @Id @Column(name = "user_id") var userId: String,
    @Id @Column(name = "group_id") var groupId: String,
    @Column var wins: Int
)