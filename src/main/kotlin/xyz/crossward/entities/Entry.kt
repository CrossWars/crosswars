package xyz.crossward.entities

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(
    name = "ENTRY",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["date", "user_id"])
    ]
)
class Entry(
    @Id @Column(name = "user_id") val userId: String,
    @Column val date: LocalDate,
    @Column val time: Int,
)