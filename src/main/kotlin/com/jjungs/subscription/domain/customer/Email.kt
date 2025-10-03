package com.jjungs.subscription.domain.customer

import com.fasterxml.jackson.annotation.JsonValue
import java.util.regex.Pattern

data class Email(@get:JsonValue val value: String) {
    companion object {
        private val EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9](\\.?[a-zA-Z0-9_-]+)+\\.[a-zA-Z]{2,}$",
        )
    }

    init {
        require(value.isNotBlank()) { "Email cannot be blank" }
        require(isValidEmail(value)) { "Invalid email format: $value" }
    }

    private fun isValidEmail(email: String): Boolean {
        return EMAIL_PATTERN.matcher(email).matches()
    }
}
