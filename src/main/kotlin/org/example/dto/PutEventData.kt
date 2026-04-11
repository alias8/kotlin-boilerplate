package org.example.dto

data class PutEventData(
    val orgId: String = "",
    val flags: List<FlagData> = emptyList()
)
