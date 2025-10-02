package com.jjungs.subscription.domain.customer

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

class CustomerId(@get:JsonValue val id: String = UUID.randomUUID().toString()) {
    constructor(uuid: UUID) : this(uuid.toString())

    init {
        require(id.isNotBlank()) { "CustomerId cannot be blank" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomerId) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return id
    }
}
