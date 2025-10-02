package com.jjungs.subscription.domain.subscription

data class PaymentMethodData(
    val type: PaymentMethodType,
    val cardNumber: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val cvv: String,
    val holderName: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaymentMethodData

        if (expiryMonth != other.expiryMonth) return false
        if (expiryYear != other.expiryYear) return false
        if (type != other.type) return false
        if (cardNumber != other.cardNumber) return false
        if (cvv != other.cvv) return false
        if (holderName != other.holderName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = expiryMonth
        result = 31 * result + expiryYear
        result = 31 * result + type.hashCode()
        result = 31 * result + cardNumber.hashCode()
        result = 31 * result + cvv.hashCode()
        result = 31 * result + holderName.hashCode()
        return result
    }
}
