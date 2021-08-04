package xyz.crossward.entities

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.persistence.*

@Entity
@Table(name = "ENTRY")
@IdClass(EntryId::class)
data class Entry(
    @Id
    @Column(name = "user_id", unique = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val userId: String,

    @Column(unique = true) val date: String? = null,
    @Column val time: Int,
) : Serializable

data class EntryId(
    val userId: String = "",
    val date: String = ""
) : Serializable

private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun Entry.localDate(): LocalDate? {
    if (this.date != null) {
        return LocalDate.parse(this.date, formatter)
    }
    return null;
}

fun LocalDate.entryDateString(): String {
    return this.format(formatter)
}
