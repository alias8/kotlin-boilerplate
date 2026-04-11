package org.example.repository

import org.example.model.Organization
import org.springframework.data.jpa.repository.JpaRepository

interface OrganizationRepository : JpaRepository<Organization, String>
