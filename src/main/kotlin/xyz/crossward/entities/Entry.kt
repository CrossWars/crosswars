package xyz.crossward.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(
    name = "ENTRY",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["date", "user_id"])
    ]
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class Entry(
    @Id @Column(name = "user_id") val userId: String,
    @Column val date: LocalDate,
    @Column val time: Int,
)