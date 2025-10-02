package com.jjungs.subscription.domain.subscription

interface SubscriptionEventPublisher {
    fun publish(event: SubscriptionCreated)
    fun publish(event: SubscriptionActivated)
    fun publish(event: SubscriptionPlanUpdated)
    fun publish(event: SubscriptionStartedTrial)
    fun publish(event: SubscriptionPaused)
    fun publish(event: SubscriptionSuspended)
    fun publish(event: SubscriptionPastDue)
    fun publish(event: SubscriptionCancelled)
    fun publish(event: SubscriptionExpired)
}
