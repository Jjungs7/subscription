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
}
