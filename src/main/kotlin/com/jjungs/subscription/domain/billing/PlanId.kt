package com.jjungs.subscription.domain.billing

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

class PlanId(@get:JsonValue val value: String = UUID.randomUUID().toString()) {
    constructor(uuid: UUID) : this(uuid.toString())

    init {
        require(value.isNotBlank()) { "PlanId cannot be blank" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlanId

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
