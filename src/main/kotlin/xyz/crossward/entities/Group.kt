package xyz.crossward.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "GROUP")
class Group(
    @Id val id: String,
    @Column val name: String
)