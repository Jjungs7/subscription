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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ValidationResult

        if (success != other.success) return false
        if (paymentMethodId != other.paymentMethodId) return false
        if (errorMessage != other.errorMessage) return false
        if (occurredAt != other.occurredAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = success.hashCode()
        result = 31 * result + paymentMethodId.hashCode()
        result = 31 * result + errorMessage.hashCode()
        result = 31 * result + occurredAt.hashCode()
        return result
    }
}
