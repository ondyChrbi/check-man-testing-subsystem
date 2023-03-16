package cz.upce.fei.testingsubsystem.domain

import jakarta.persistence.*

@Entity
data class Review (
    @field:Id @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @field:Column var description: String = "",
    @field:Column var active: Boolean = true,
    @field:Column var published: Boolean = false,
    @field:OneToOne(fetch = FetchType.EAGER)
    @field:JoinColumn(name = "solution_id", nullable = true)
    var solution: Solution? = null,
    @field:ManyToMany(mappedBy = "reviews", fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var feedbacks: MutableCollection<Feedback> = mutableSetOf()
)