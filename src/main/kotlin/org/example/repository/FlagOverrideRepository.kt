package org.example.repository

import org.example.model.FlagOverride
import org.springframework.data.jpa.repository.JpaRepository

interface FlagOverrideRepository : JpaRepository<FlagOverride, String> {
    fun deleteByFlagId(flagId: String)
}
