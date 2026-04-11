package org.example.model

import jakarta.persistence.*

@Entity
@Table(name = "flag_overrides")
class FlagOverride(
    @Id
    @Column(columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String = "",

    @Column(name = "user_id")
    var userId: String = "",

    var override: Boolean = false,

    @Column(name = "flag_id", columnDefinition = "uuid", nullable = false)
    var flagId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_id", insertable = false, updatable = false)
    var flag: Flag? = null
)
