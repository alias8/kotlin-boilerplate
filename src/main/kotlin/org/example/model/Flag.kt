package org.example.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "flags",
    uniqueConstraints = [UniqueConstraint(columnNames = ["org_id", "name"])]
)
class Flag(
    @Id
    @Column(columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String = "",

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var enabled: Boolean = false,

    @Column(name = "org_id", columnDefinition = "uuid", nullable = false)
    var orgId: String = "",

    @Column(name = "created_at")
    var createdAt: Instant = Instant.now(),

    @Column(name = "rollout_percentage")
    var rolloutPercentage: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", insertable = false, updatable = false)
    var organization: Organization? = null,

    @OneToMany(mappedBy = "flag", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var flagOverrides: MutableList<FlagOverride> = mutableListOf()
)
