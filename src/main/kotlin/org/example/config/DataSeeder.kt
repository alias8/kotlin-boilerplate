package org.example.config

import org.example.model.ApiKey
import org.example.model.Flag
import org.example.model.Organization
import org.example.repository.ApiKeyRepository
import org.example.repository.FlagRepository
import org.example.repository.OrganizationRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class DataSeeder(
    private val organizationRepository: OrganizationRepository,
    private val apiKeyRepository: ApiKeyRepository,
    private val flagRepository: FlagRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        // Organization 1
        val org1Id = "550e8400-e29b-41d4-a716-446655440000"
        if (!organizationRepository.existsById(org1Id)) {
            organizationRepository.save(Organization(orgId = org1Id, name = "Test Org 1"))
        }
        if (apiKeyRepository.findByToken("test-token-1") == null) {
            apiKeyRepository.save(
                ApiKey(id = "660e8400-e29b-41d4-a716-446655440000", token = "test-token-1", orgId = org1Id)
            )
        }
        if (flagRepository.findByOrgIdAndName(org1Id, "darkMode") == null) {
            flagRepository.save(Flag(name = "darkMode", enabled = true, orgId = org1Id))
        }
        if (flagRepository.findByOrgIdAndName(org1Id, "betaFeatures") == null) {
            flagRepository.save(Flag(name = "betaFeatures", enabled = false, orgId = org1Id))
        }

        // Organization 2
        val org2Id = "550e8400-e29b-41d4-a716-446655440001"
        if (!organizationRepository.existsById(org2Id)) {
            organizationRepository.save(Organization(orgId = org2Id, name = "Test Org 2"))
        }
        if (apiKeyRepository.findByToken("test-token-2") == null) {
            apiKeyRepository.save(
                ApiKey(id = "660e8400-e29b-41d4-a716-446655440001", token = "test-token-2", orgId = org2Id)
            )
        }
        if (flagRepository.findByOrgIdAndName(org2Id, "newUI") == null) {
            flagRepository.save(Flag(name = "newUI", enabled = true, orgId = org2Id))
        }
        if (flagRepository.findByOrgIdAndName(org2Id, "analytics") == null) {
            flagRepository.save(Flag(name = "analytics", enabled = true, orgId = org2Id))
        }

        println("Seed data ready")
    }
}
