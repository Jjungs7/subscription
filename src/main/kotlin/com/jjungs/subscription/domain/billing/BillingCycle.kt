package com.jjungs.subscription.domain.billing

import java.time.OffsetDateTime

enum class BillingCycleType {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}

class BillingCycle(
    val type: BillingCycleType,
    val interval: Int = 1,
) : Comparable<BillingCycle> {
    init {
        require(interval > 0) { "Billing cycle interval must be positive: $interval" }
    }

    companion object {
        fun daily(): BillingCycle = BillingCycle(BillingCycleType.DAILY)
        fun weekly(): BillingCycle = BillingCycle(BillingCycleType.WEEKLY)
        fun monthly(): BillingCycle = BillingCycle(BillingCycleType.MONTHLY)
        fun yearly(): BillingCycle = BillingCycle(BillingCycleType.YEARLY)
    }

    fun calculateNextBillingDate(currentDate: OffsetDateTime): OffsetDateTime {
        return when (type) {
            BillingCycleType.DAILY -> currentDate.plusDays(interval.toLong())
            BillingCycleType.WEEKLY -> currentDate.plusWeeks(interval.toLong())
            BillingCycleType.MONTHLY -> currentDate.plusMonths(interval.toLong())
            BillingCycleType.YEARLY -> currentDate.plusYears(interval.toLong())
        }
    }

    override fun compareTo(other: BillingCycle): Int {
        val typeComparison = type.compareTo(other.type)
        return if (typeComparison != 0) {
            typeComparison
        } else {
            interval.compareTo(other.interval)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BillingCycle

        return type == other.type && interval == other.interval
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + interval
        return result
    }
}
