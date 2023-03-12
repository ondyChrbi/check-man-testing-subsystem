package cz.upce.fei.testingsubsystem.domain

import jakarta.persistence.*

@Entity
@Table(name = "global_role")
data class Role(
    @field:Id private var id: Long,
    var name: String,
    @field:ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    var appUsers: MutableCollection<AppUser> = mutableListOf()
)
