package com.jjungs.subscription.domain.customer

import com.jjungs.subscription.domain.vo.Email
import java.time.OffsetDateTime

interface CustomerDomainEvent {
    val customerId: CustomerId
    val occurredAt: OffsetDateTime
    val eventType: String
        get() = this::class.simpleName ?: "UnknownEvent"
}

data class CustomerCreated(
    override val customerId: CustomerId,
    override val occurredAt: OffsetDateTime,

    val email: Email,
) : CustomerDomainEvent

data class CustomerActivated(
    override val customerId: CustomerId,
    override val occurredAt: OffsetDateTime,

    val email: Email,
) : CustomerDomainEvent

data class CustomerSuspended(
    override val customerId: CustomerId,
    override val occurredAt: OffsetDateTime,

    val email: Email,
) : CustomerDomainEvent

data class CustomerDeactivated(
    override val customerId: CustomerId,
    override val occurredAt: OffsetDateTime,

    val email: Email,
) : CustomerDomainEvent

data class CustomerEmailUpdated(
    override val customerId: CustomerId,
    override val occurredAt: OffsetDateTime,

    val oldEmail: Email,
    val newEmail: Email,
) : CustomerDomainEvent
