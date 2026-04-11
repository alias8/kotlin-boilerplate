package org.example.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "organizations")
class Organization(
    @Id
    @Column(name = "org_id", columnDefinition = "uuid")
    var orgId: String = "",

    @Column(nullable = false)
    var name: String = "",

    @Column(name = "created_at")
    var createdAt: Instant = Instant.now(),

    @OneToMany(mappedBy = "organization", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var apiKeys: MutableList<ApiKey> = mutableListOf(),

    @OneToMany(mappedBy = "organization", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var flags: MutableList<Flag> = mutableListOf()
)
