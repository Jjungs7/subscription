package com.jjungs.subscription.domain.billing

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

class PlanId(@get:JsonValue val id: String = UUID.randomUUID().toString()) {
    constructor(uuid: UUID) : this(uuid.toString())

    init {
        require(id.isNotBlank()) { "PlanId cannot be blank" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlanId

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
