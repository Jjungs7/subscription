package com.jjungs.subscription.domain.subscription

import com.jjungs.subscription.domain.billing.Money
import java.time.OffsetDateTime

data class PaymentResult(
    val success: Boolean,
    val paymentId: String,
    val amount: Money,
    val errorMessage: String = "",
    val occurredAt: OffsetDateTime = OffsetDateTime.now(),
) {
    companion object {
        fun success(paymentId: String, amount: Money): PaymentResult {
            return PaymentResult(true, paymentId, amount)
        }

        fun failure(paymentId: String, errorMessage: String, amount: Money): PaymentResult {
            return PaymentResult(false, paymentId, amount, errorMessage)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaymentResult

        if (success != other.success) return false
        if (paymentId != other.paymentId) return false
        if (amount != other.amount) return false
        if (errorMessage != other.errorMessage) return false
        if (occurredAt != other.occurredAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = success.hashCode()
        result = 31 * result + paymentId.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + errorMessage.hashCode()
        result = 31 * result + occurredAt.hashCode()
        return result
    }

}
