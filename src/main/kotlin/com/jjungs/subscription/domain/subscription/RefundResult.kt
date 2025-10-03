package com.jjungs.subscription.domain.subscription

import com.jjungs.subscription.domain.billing.Money

data class RefundResult(
    val success: Boolean,
    val paymentId: String,
    val amount: Money,
    val errorMessage: String = "",
)
