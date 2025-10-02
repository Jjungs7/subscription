package com.jjungs.subscription.domain.subscription

import com.jjungs.subscription.domain.billing.Money

data class RefundResult(
    val success: Boolean,
    val paymentId: String,
    val amount: Money,
    val errorMessage: String = "",
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RefundResult

        if (success != other.success) return false
        if (paymentId != other.paymentId) return false
        if (amount != other.amount) return false
        if (errorMessage != other.errorMessage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = success.hashCode()
        result = 31 * result + paymentId.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + errorMessage.hashCode()
        return result
    }
}
