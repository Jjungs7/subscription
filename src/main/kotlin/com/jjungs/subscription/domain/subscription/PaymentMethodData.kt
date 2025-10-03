package com.jjungs.subscription.domain.subscription

data class PaymentMethodData(
    val type: PaymentMethodType,
    val cardNumber: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val cvv: String,
    val holderName: String,
)
