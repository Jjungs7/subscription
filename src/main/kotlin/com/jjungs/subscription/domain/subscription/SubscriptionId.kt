package com.jjungs.subscription.domain.subscription

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

data class SubscriptionId(@get:JsonValue val id: String = UUID.randomUUID().toString()) {
    constructor(uuid: UUID) : this(uuid.toString())

    init {
        require(id.isNotBlank()) { "SubscriptionId cannot be blank" }
    }
}
