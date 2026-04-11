package org.example.repository

import org.example.model.Flag
import org.springframework.data.jpa.repository.JpaRepository

interface FlagRepository : JpaRepository<Flag, String> {
    fun findByOrgIdAndName(orgId: String, name: String): Flag?
    fun findByOrgId(orgId: String): List<Flag>
}
