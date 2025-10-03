package com.jjungs.subscription.domain.subscription

import java.time.OffsetDateTime

data class PaymentMethodResult(
    val success: Boolean,
    val paymentMethodId: String,
    val errorMessage: String = "",
    val occurredAt: OffsetDateTime = OffsetDateTime.now(),
) {
    companion object {
        fun success(paymentMethodId: String): PaymentMethodResult {
            return PaymentMethodResult(true, paymentMethodId)
        }

        fun failure(paymentMethodId: String, message: String): PaymentMethodResult {
            return PaymentMethodResult(false, paymentMethodId, message)
        }
    }
}
