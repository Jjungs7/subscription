package com.jjungs.subscription.domain.customer

import com.jjungs.subscription.domain.vo.Name
import com.jjungs.subscription.domain.vo.Phone
import com.jjungs.subscription.domain.vo.setFirstName
import com.jjungs.subscription.domain.vo.setLastName
import java.time.OffsetDateTime

class CustomerProfile(
    val customerId: CustomerId,
    var name: Name,
    var phoneNumber: Phone,
    var address: String = "",
    var dateOfBirth: OffsetDateTime? = null,
) {
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    var updatedAt: OffsetDateTime = createdAt
        private set

    fun updateFirstName(newFirstName: String) {
        require(newFirstName.isNotBlank()) { "First name cannot be blank" }
        name = name.setFirstName(newFirstName)
        updateTimestamp()
    }

    fun updateLastName(newLastName: String) {
        require(newLastName.isNotBlank()) { "Last name cannot be blank" }
        name = name.setLastName(newLastName)
        updateTimestamp()
    }

    fun updatePhoneNumber(newPhoneNumber: Phone) {
        phoneNumber = newPhoneNumber
        updateTimestamp()
    }

    fun updateAddress(newAddress: String) {
        address = newAddress
        updateTimestamp()
    }

    fun updateDateOfBirth(newDateOfBirth: OffsetDateTime?) {
        dateOfBirth = newDateOfBirth
        updateTimestamp()
    }

    private fun updateTimestamp() {
        updatedAt = OffsetDateTime.now()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomerProfile) return false
        return customerId == other.customerId
    }

    override fun hashCode(): Int {
        return customerId.hashCode()
    }
}
