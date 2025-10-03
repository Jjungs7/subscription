package com.jjungs.subscription.domain.subscription

data class PaymentMethod(
    val id: String,
    val type: PaymentMethodType,
    val lastFourDigits: String,
    val expiryMonth: Int?,
    val expiryYear: Int?,
    val isDefault: Boolean = false,
)
