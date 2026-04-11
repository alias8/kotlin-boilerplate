package org.example.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "api_keys")
class ApiKey(
    @Id
    @Column(columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String = "",

    @Column(unique = true, nullable = false)
    var token: String = "",

    @Column(name = "org_id", columnDefinition = "uuid", nullable = false)
    var orgId: String = "",

    @Column(name = "created_at")
    var createdAt: Instant = Instant.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", insertable = false, updatable = false)
    var organization: Organization? = null
)
