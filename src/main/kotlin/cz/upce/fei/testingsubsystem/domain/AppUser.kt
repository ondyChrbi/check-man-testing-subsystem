package cz.upce.fei.testingsubsystem.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class AppUser(
    @field:Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    var stagId: String = "",
    var mail: String = "",
    var displayName: String = "",
    var registrationDate: LocalDateTime = LocalDateTime.now(),
    var lastAccessDate: LocalDateTime = LocalDateTime.now(),
    var disabled: Boolean = false,
    @field:ManyToMany(fetch = FetchType.EAGER)
    @field:JoinTable(
        name = "app_user_global_role",
        joinColumns = [JoinColumn(name = "app_user_id")],
        inverseJoinColumns = [JoinColumn(name = "global_role_id")]
    )
    var roles: MutableCollection<Role> = mutableListOf(),
    @field:OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var solutions: MutableCollection<Solution> = mutableListOf()
) {
    override fun toString(): String {
        return "AppUser(id=$id, stagId='$stagId', mail='$mail', displayName='$displayName', registrationDate=$registrationDate, lastAccessDate=$lastAccessDate, disabled=$disabled)"
    }
}
