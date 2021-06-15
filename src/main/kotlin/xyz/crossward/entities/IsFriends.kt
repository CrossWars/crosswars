package xyz.crossward.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.io.Serializable
import javax.persistence.*

/**
 * Must implement Serializable for composite keys to work
 */
@Entity
@Table(name = "IS_FRIENDS")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class IsFriends(
    @Id @Column(name = "user_id_1")
    val userId1: String,

    @Id @Column(name = "user_id_2")
    val userId2: String
): Serializable