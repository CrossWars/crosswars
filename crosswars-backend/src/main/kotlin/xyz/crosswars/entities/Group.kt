package xyz.crosswars.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "CROSSGROUP")
data class Group(
        @Id @Column(name = "group_id") val id: String,
        @Column val name: String,
        @Column(name = "created_by") val createdBy: String?
)