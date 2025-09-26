package com.jjungs.subscription.domain.customer

interface CustomerEventPublisher {
    fun publish(event: CustomerCreated)
    fun publish(event: CustomerActivated)
    fun publish(event: CustomerSuspended)
    fun publish(event: CustomerDeactivated)
    fun publish(event: CustomerEmailUpdated)
}
