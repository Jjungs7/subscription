package com.jjungs.subscription.domain.billing

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

data class PlanId(@get:JsonValue val value: String = UUID.randomUUID().toString()) {
    constructor(uuid: UUID) : this(uuid.toString())

    init {
        require(value.isNotBlank()) { "PlanId cannot be blank" }
    }
}
