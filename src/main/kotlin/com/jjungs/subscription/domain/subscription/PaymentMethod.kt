package com.jjungs.subscription.domain.subscription

data class PaymentMethod(
    val id: String,
    val type: PaymentMethodType,
    val lastFourDigits: String,
    val expiryMonth: Int?,
    val expiryYear: Int?,
    val isDefault: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaymentMethod

        if (expiryMonth != other.expiryMonth) return false
        if (expiryYear != other.expiryYear) return false
        if (isDefault != other.isDefault) return false
        if (id != other.id) return false
        if (type != other.type) return false
        if (lastFourDigits != other.lastFourDigits) return false

        return true
    }

    override fun hashCode(): Int {
        var result = expiryMonth ?: 0
        result = 31 * result + (expiryYear ?: 0)
        result = 31 * result + isDefault.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + lastFourDigits.hashCode()
        return result
    }
}
