package xyz.crossward.entities

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.persistence.*

@Entity
@Table(
    name = "ENTRY",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["date", "user_id"])
    ]
)
data class Entry(
    @Id @Column(name = "user_id") val userId: String,
    @Column val date: String? = null,
    @Column val time: Int,
)

private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun Entry.localDate(): LocalDate? {
    return LocalDate.parse(this.date, formatter)
}

fun LocalDate.entryDateString(): String {
    return this.format(formatter)
}
