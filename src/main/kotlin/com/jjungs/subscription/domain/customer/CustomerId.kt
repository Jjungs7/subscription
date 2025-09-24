package com.jjungs.subscription.domain.customer

import java.util.*

class CustomerId(val value: String = UUID.randomUUID().toString()) {
    constructor(uuid: UUID) : this(uuid.toString())

    init {
        require(value.isNotBlank()) { "CustomerId cannot be blank" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomerId) return false
        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value
    }
}
