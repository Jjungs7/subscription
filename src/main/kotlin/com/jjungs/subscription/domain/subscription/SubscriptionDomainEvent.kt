package com.jjungs.subscription.domain.subscription

import com.jjungs.subscription.domain.billing.PlanId
import java.time.OffsetDateTime

interface SubscriptionDomainEvent {
    val subscriptionId: SubscriptionId
    val occurredAt: OffsetDateTime
    val eventType: String
        get() = this::class.simpleName ?: "UnknownEvent"
}

data class SubscriptionCreated(
    override val subscriptionId: SubscriptionId,
    override val occurredAt: OffsetDateTime,
) : SubscriptionDomainEvent

data class SubscriptionActivated(
    override val subscriptionId: SubscriptionId,
    override val occurredAt: OffsetDateTime,
) : SubscriptionDomainEvent

data class SubscriptionPlanUpdated(
    override val subscriptionId: SubscriptionId,
    override val occurredAt: OffsetDateTime,

    val oldPlanId: PlanId,
    val newPlanId: PlanId,
) : SubscriptionDomainEvent

data class SubscriptionStartedTrial(
    override val subscriptionId: SubscriptionId,
    override val occurredAt: OffsetDateTime,

    val trialEndDate: OffsetDateTime,
) : SubscriptionDomainEvent

data class SubscriptionPaused(
    override val subscriptionId: SubscriptionId,
    override val occurredAt: OffsetDateTime,

    val pausedEndDate: OffsetDateTime,
) : SubscriptionDomainEvent

data class SubscriptionSuspended(
    override val subscriptionId: SubscriptionId,
    override val occurredAt: OffsetDateTime,
) : SubscriptionDomainEvent

data class SubscriptionPastDue(
    override val subscriptionId: SubscriptionId,
    override val occurredAt: OffsetDateTime,
) : SubscriptionDomainEvent

data class SubscriptionCancelled(
    override val subscriptionId: SubscriptionId,
    override val occurredAt: OffsetDateTime,
) : SubscriptionDomainEvent

data class SubscriptionExpired(
    override val subscriptionId: SubscriptionId,
    override val occurredAt: OffsetDateTime,
) : SubscriptionDomainEvent
