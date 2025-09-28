package com.jjungs.subscription.interfaces.exceptions

import com.jjungs.subscription.application.customer.CustomerNotFoundException
import com.jjungs.subscription.infrastructure.customer.CustomerConcurrentModificationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class CommonControllerAdvice {

    @ExceptionHandler(CustomerNotFoundException::class)
    fun handleCustomerNotFoundException(e: CustomerNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                error = "CUSTOMER_NOT_FOUND",
                message = e.message ?: "Customer not found",
                customerId = e.customerId.toString(),
            ),
        )
    }

    @ExceptionHandler(CustomerConcurrentModificationException::class)
    fun handleCustomerConcurrentModificationException(e: CustomerConcurrentModificationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                error = "CONCURRENT_MODIFICATION_EXCEPTION",
                message = e.message ?: "Status of Customer has changed. Please refresh the page and try it again.",
                customerId = e.customerId.toString(),
            ),
        )
    }
}

data class ErrorResponse(
    val error: String,
    val message: String,
    val customerId: String? = null,
)
