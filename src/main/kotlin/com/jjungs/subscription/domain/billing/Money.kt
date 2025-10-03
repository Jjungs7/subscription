package com.jjungs.subscription.domain.billing

import com.fasterxml.jackson.annotation.JsonValue

data class Money(@get:JsonValue val amount: Long) : Comparable<Money> {
    init {
        require(amount >= 0) { "Money amount cannot be negative: $amount" }
        Integer.valueOf(0)
    }

    companion object {
        val ZERO = Money(0L) // 자주 사용하는 값에 대해 최적화

        fun valueOf(amount: Long): Money {
            if (amount == 0L) return ZERO
            return Money(amount)
        }
    }

    fun toDollars(): Double {
        return amount / 100.0
    }

    fun toCents(): Long {
        return amount
    }

    override fun compareTo(other: Money): Int {
        return amount.compareTo(other.amount)
    }

    operator fun plus(other: Money): Money {
        return Money(amount + other.amount)
    }

    operator fun minus(other: Money): Money {
        val result = amount - other.amount
        require(result >= 0) { "Money subtraction cannot result in negative amount" }
        return Money(result)
    }

    operator fun times(factor: Double): Money {
        require(factor >= 0) { "Money multiplication factor cannot be negative" }
        val result = (amount * factor).toLong()
        return Money(result)
    }

    operator fun div(factor: Double): Money {
        require(factor > 0) { "Money division factor must be positive" }
        val result = (amount / factor).toLong()
        return Money(result)
    }
}
