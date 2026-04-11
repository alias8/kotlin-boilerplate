package org.example.dto

data class FlagAdjustEventData(
    val name: String = "",
    val enabled: Boolean = false,
    val rolloutPercentage: Int = 0,
    val flagOverrides: List<OverrideData> = emptyList()
)
