package com.jjungs.subscription.domain.subscription

import com.jjungs.subscription.domain.billing.Money
import com.jjungs.subscription.domain.customer.CustomerId

interface PaymentPort {
    fun processPayment(customerId: CustomerId, amount: Money, description: String): PaymentResult
    fun refundPayment(paymentId: String, amount: Money): RefundResult
    fun createPaymentMethod(customerId: CustomerId, paymentMethodData: PaymentMethodData): PaymentMethodResult
    fun updatePaymentMethod(
        customerId: CustomerId,
        paymentMethodId: String,
        paymentMethodData: PaymentMethodData,
    ): PaymentMethodResult

    fun deletePaymentMethod(customerId: CustomerId, paymentMethodId: String): Boolean
    fun getPaymentMethods(customerId: CustomerId): List<PaymentMethod>
    fun validatePaymentMethod(paymentMethodData: PaymentMethodData): ValidationResult
}
