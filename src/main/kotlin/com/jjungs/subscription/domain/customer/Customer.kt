package com.jjungs.subscription.domain.customer

import com.jjungs.subscription.domain.vo.Email
import java.time.LocalDateTime

class Customer(
    val id: CustomerId,
    var email: Email,
) {
    val createdAt: LocalDateTime = LocalDateTime.now()

    var status: CustomerStatus = CustomerStatus.PENDING
        private set

    var updatedAt: LocalDateTime = createdAt
        private set

    fun activate() {
        status = CustomerStatus.ACTIVE
        updateTimestamp()
    }

    fun suspend() {
        status = CustomerStatus.SUSPENDED
        updateTimestamp()
    }

    fun deactivate() {
        status = CustomerStatus.INACTIVE
        updateTimestamp()
    }

    fun updateEmail(newEmail: Email) {
        email = newEmail
        updateTimestamp()
    }

    private fun updateTimestamp() {
        updatedAt = LocalDateTime.now()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Customer) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
