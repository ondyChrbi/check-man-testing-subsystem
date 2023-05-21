package cz.upce.fei.testingsubsystem.domain.testing

import jakarta.persistence.*

@Entity
@Table
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
) {
    override fun toString(): String {
        return "Review(id=$id, description='$description', active=$active, published=$published)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Review

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }


}