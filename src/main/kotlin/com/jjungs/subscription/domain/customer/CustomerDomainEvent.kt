package com.jjungs.subscription.domain.customer

import com.jjungs.subscription.domain.vo.Email
import java.time.LocalDateTime

interface CustomerDomainEvent {
    val customerId: CustomerId
    val occuredAt: LocalDateTime
}

data class CustomerCreated(
    override val customerId: CustomerId,
    override val occuredAt: LocalDateTime,

    val email: Email,
) : CustomerDomainEvent

data class CustomerActivated(
    override val customerId: CustomerId,
    override val occuredAt: LocalDateTime,

    val email: Email,
) : CustomerDomainEvent

data class CustomerSuspended(
    override val customerId: CustomerId,
    override val occuredAt: LocalDateTime,

    val email: Email,
) : CustomerDomainEvent

data class CustomerDeactivated(
    override val customerId: CustomerId,
    override val occuredAt: LocalDateTime,

    val email: Email,
) : CustomerDomainEvent

data class CustomerEmailUpdated(
    override val customerId: CustomerId,
    override val occuredAt: LocalDateTime,

    val oldEmail: Email,
    val newEmail: Email,
) : CustomerDomainEvent
