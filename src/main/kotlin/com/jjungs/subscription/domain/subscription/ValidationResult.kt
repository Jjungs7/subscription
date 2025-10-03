package com.jjungs.subscription.domain.subscription

import java.time.OffsetDateTime

data class ValidationResult(
    val success: Boolean,
    val paymentMethodId: String,
    val errorMessage: String = "",
    val occurredAt: OffsetDateTime = OffsetDateTime.now(),
) {
    companion object {
        fun success(paymentMethodId: String): ValidationResult {
            return ValidationResult(true, paymentMethodId)
        }

        fun failure(paymentMethodId: String, errorMessage: String): ValidationResult {
            return ValidationResult(false, paymentMethodId, errorMessage)
        }
    }
}
