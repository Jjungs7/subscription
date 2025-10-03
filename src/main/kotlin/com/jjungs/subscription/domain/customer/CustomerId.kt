package com.jjungs.subscription.domain.customer

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

data class CustomerId(@get:JsonValue val id: String = UUID.randomUUID().toString()) {
    constructor(uuid: UUID) : this(uuid.toString())

    init {
        require(id.isNotBlank()) { "CustomerId cannot be blank" }
    }
}
